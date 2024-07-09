<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import= "java.util.List"%>
<%@ page import="org.apache.logging.log4j.LogManager" %>
<%@ page import="org.apache.logging.log4j.Logger" %>
<%@ page import="com.sist.web.util.HttpUtil" %>
<%@ page import="com.sist.web.projdao.BoardDao" %>
<%@ page import="com.sist.web.projdao.CommentDao" %>
<%@ page import="com.sist.web.proj.Board" %>
<%@ page import="com.sist.web.proj.Comment" %>
<%@ page import="com.sist.common.util.StringUtil" %>
<%@ page import="com.sist.web.model.Paging" %>
<%@ page import="com.sist.web.model.BoardFileConfig" %>
<%
	Logger logger = LogManager.getLogger("/board/list.jsp");
	HttpUtil.requestLogString(request, logger);
	
	String searchType = HttpUtil.get(request, "searchType", "");
	String searchValue = HttpUtil.get(request, "searchValue", "");
	long curPage = HttpUtil.get(request, "curPage", (long)1);
	
	long totalCount = 0;
	List<Board> list = null;
	Paging paging = null;
	
	BoardDao boaDao = new BoardDao();
	Board search = new Board();
	
	
	if(!StringUtil.isEmpty(searchType) && !StringUtil.isEmpty(searchValue))
	{
		if(StringUtil.equals(searchType, "1"))
		{
			search.setMemName(searchValue);
		}
		if(StringUtil.equals(searchType, "2"))
		{
			search.setBoaTitle(searchValue);
		}
		if(StringUtil.equals(searchType, "3"))
		{
			search.setBoaContent(searchValue);
		}
	}
	
	totalCount = boaDao.totalBoaCount(search);
	
	logger.debug("총 게시물 수 : " + totalCount);
	
	if(totalCount > 0)
	{
		paging = new Paging(totalCount, BoardFileConfig.LIST_COUNT, BoardFileConfig.PAGE_COUNT, curPage);
		search.setStartRow(paging.getStartRow());
		search.setEndRow(paging.getEndRow());
		
		list = boaDao.boardList(search);
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@ include file="/include/head.jsp" %>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Bagel+Fat+One&display=swap" rel="stylesheet">
<style>
a {color:black;}
a:hover{color:lightblue;}
th{background-color: white}
h2{position:center; font-family: 'Bagel Fat One', system-ui; font-size:5rem}
a.page-link{background-color: black; color:white;}
a.main-title:hover{text-decoration:none; }
a.page-link:hover{background-color:lightblue; color:white}
.navbar{background-color: black;}
.boa-title{width:100%;text-align: center;margin: 20px;padding:20px}
.btn {background-color: black;}
.btn:hover{background-color:lightblue; border-color: white;}
.navba{background-color: black;}
</style>
<script>
$(document).ready(function(){
	
	$("#_searchType").change(function(){
		$("#_searchValue").val("");
	});
	
	$('#_searchValue').on('keypress', function(e){
		  if(e.keyCode == '13'){
		  	$('#btnSearch').click();
		  }
	});
	
	$("#btnSearch").on("click", function(){
		
		if($("#_searchType").val() == "")
		{
			if($.trim($("#_searchValue").val()).length > 0)
			{
				alert("조회 항목을 선택 해주세요.");
				$("#_searchValue").val("");
				$("#_searchType").focus();
				return;
			}
		}
		
		if($("#_searchType").val() != "")
		{
			if($.trim($("#_searchValue").val()) == "")
			{
				alert("조회 내용을 입력해주세요.");
				$("#_searchValue").val("");
				$("#_searchValue").focus();
				return;
			}
		}
		
		document.bbsForm.bbsSeq.value = "";
		document.bbsForm.searchType.value = $("#_searchType").val();
		document.bbsForm.searchValue.value = $("#_searchValue").val();
		document.bbsForm.curPage.value = "";
		document.bbsForm.action = "/board/list.jsp";
		document.bbsForm.submit();
	});
	
	$("#btnWrite").on("click",function(){
		document.bbsForm.bbsSeq.value = "";
		document.bbsForm.action = "/board/write.jsp";
		document.bbsForm.submit();
	});
	
});
function fn_list(curPage)
{
	document.bbsForm.bbsSeq.value = "";
	document.bbsForm.curPage.value = curPage;
	document.bbsForm.action = "/board/list.jsp";
	document.bbsForm.submit();
};

function fn_view(bbsSeq)
{
	document.bbsForm.bbsSeq.value = bbsSeq;
	document.bbsForm.action = "/board/view.jsp";
	document.bbsForm.submit();
}
</script>
</head>
<body>
<%@ include file="/include/navigation.jsp" %>
<div class="container">
   <div class="">
      <div class="boa-title">
         <h2>BOARD LIST</h2>
      </div>
      <div class="ml-auto input-group" style="width:50%;">
         <select name="_searchType" id="_searchType" class="custom-select" style="width:auto;">
            <option value="">조회 항목</option>
            <option value="1" <%if(StringUtil.equals(searchType, "1")){%>selected<%}%>>작성자</option>
            <option value="2" <%if(StringUtil.equals(searchType, "2")){%>selected<%}%>>제목</option>
            <option value="3" <%if(StringUtil.equals(searchType, "3")){%>selected<%}%>>내용</option>
         </select>
         
         <input type="text" name="_searchValue" id="_searchValue" value="<%=searchValue%>" class="form-control mx-1" maxlength="20" style="width:auto;ime-mode:active;" placeholder="조회값을 입력하세요." />
         <button type="button" id="btnSearch" class="btn btn-secondary mb-3 mx-1">조회</button>
      </div>
    </div>
    
   <table class="table table-hover">
      <thead>
      <tr style="background-color: #dee2e6;">
         <th scope="col" class="text-center" style="width:10%">번호</th>
         <th scope="col" class="text-center" style="width:40%">제목</th>
         <th scope="col" class="text-center" style="width:10%">작성자</th>
         <th scope="col" class="text-center" style="width:20%">날짜</th>
         <th scope="col" class="text-center" style="width:10%">조회수</th>
         <th scope="col" class="text-center" style="width:10%">추천수</th>
      </tr>
      </thead>
      <tbody>
<%
if(list != null && list.size() > 0)
	{
		long startNum = paging.getStartNum();
		CommentDao commdao = new CommentDao();
		
		for(int i = 0; i < list.size(); i++)
		{
			Board boa = list.get(i);
			long commCount = commdao.countComm(boa.getBoaNum());
			logger.debug(commCount + boa.getBoaNum());
%>			
      <tr>
         <td class="text-center"><%=startNum%></td>
         <td><a href="javascript:void(0)" onclick="fn_view(<%=boa.getBoaNum()%>)"><%=boa.getBoaTitle()%></a></td>
         <td class="text-center"><%=boa.getMemName()%></td>
         <td class="text-center"><%=boa.getRegDate()%></td>
         <td class="text-center"><%=StringUtil.toNumberFormat(boa.getBoaCnt())%></td>
         <td class="text-center"><%=StringUtil.toNumberFormat(boa.getBoaLike())%></td>
      </tr>
<%
			startNum--;
		}
	}
	else
	{
%>
	<tr>
		<td colspan = "5" class="text-center">조회된 데이터가 없습니다.</td>
	</tr>
<%
	}
%>
      </tbody>
      <tfoot>
      <tr>
            <td colspan="5"></td>
        </tr>
      </tfoot>
   </table>
   <nav>
      <ul class="pagination justify-content-center">
<%
	if(paging != null)
	{
		if(paging.getPrevBlockPage() > 0)
		{
%>
         <li class="page-item"><a class="page-link" href="javascript:void(0)" onclick="fn_list(<%=paging.getPrevBlockPage()%>)"><</a></li>
<%
		}
		for(long i = paging.getStartPage(); i <= paging.getEndPage(); i++)
		{
			if(paging.getCurPage() != i)
			{
%>         
         <li class="page-item"><a class="page-link" href="javascript:void(0)" onclick="fn_list(<%=i%>)"><%=i%></a></li>
<%
			}
			else
			{
%>
         <li class="page-item active"><a class="page-link" href="javascript:void(0)" style="cursor:default; background-color:white; color:black; border-color:black"><%=i%></a></li>
<%		 
			}
		}
		
		if(paging.getNextBlockPage() > 0)
		{
%>      
         <li class="page-item"><a class="page-link" href="javascript:void(0)" onclick="fn_list(<%=paging.getNextBlockPage()%>)">></a></li>
<%
		}
	}
%>   
      </ul>
   </nav>
   
   <button type="button" id="btnWrite" class="btn btn-secondary mb-3">글쓰기</button>
   
   <form name="bbsForm" id="bbsForm" method="post">	
   		<input type="hidden" name="bbsSeq" value="">
   		<input type="hidden" name="searchType" value="<%=searchType%>">
   		<input type="hidden" name="searchValue" value="<%=searchValue%>">
   		<input type="hidden" name="curPage" value="<%=curPage%>">
   </form>
   
</div>
</body>
</html>