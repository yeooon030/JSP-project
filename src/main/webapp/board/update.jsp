<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.logging.log4j.LogManager" %>
<%@ page import="org.apache.logging.log4j.Logger" %>
<%@ page import="com.sist.web.util.HttpUtil" %>
<%@ page import="com.sist.web.util.CookieUtil" %>
<%@ page import="com.sist.web.projdao.BoardDao" %>
<%@ page import="com.sist.web.proj.Board" %>
<%@ page import="com.sist.common.util.StringUtil" %>
<%
	Logger logger = LogManager.getLogger("/board/update.jsp");
	HttpUtil.requestLogString(request, logger);
	
	String cookieUserId = CookieUtil.getValue(request, "MEM_ID");
	long boaNum = HttpUtil.get(request, "bbsSeq", (long)0);
	String searchType = HttpUtil.get(request, "searchType", "");
	String searchValue = HttpUtil.get(request, "searchValue", "");
	long curPage = HttpUtil.get(request, "curPage", (long)1);
	
	BoardDao boadao = new BoardDao();
	Board boa = boadao.viewBoa(boaNum);
%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/include/head.jsp" %>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Bagel+Fat+One&display=swap" rel="stylesheet">
<style>
a {color:black;}
a:hover{color:lightblue;}
th{background-color: white}
h2{position:center; font-family: 'Bagel Fat One', system-ui; font-size:4rem}
a.page-link{background-color: black; color:white;}
a.page-link:hover{background-color:lightblue;color:white}
.navbar{background-color: black;}
.boa-title{width:100%;text-align: center;margin: 20px;padding:20px}
.btn {background-color: black; border:none}
.btn:hover{background-color:lightblue; border-color: white;}
.navba{background-color: black;}
</style>
<script>
$(document).ready(function(){
<%
 	if(boa == null)
 	{
%>
		alert("게시물이 존재하지 않습니다.");
		location.href("/board/list.jsp");
<%
 	}
 	else
 	{
%> 			
		$("#bbsTitle").focus();
		
 		$("#btnUpdate").on("click", function(){
 			
 			if($.trim($("#bbsTitle").val()).length <= 0)
 			{
 				alert("제목을 입력해주세요.");
 				$("#bbsTitle").val("");
 				$("#bbsTitle").focus();
 				return;
 			}
 			
 			if($.trim($("#bbsContent").val()).length <= 0)
 			{
 				alert("내용을 입력해주세요.");
 				$("#bbsContent").val("");
 				$("#bbsContent").focus();
 				return;
 			}
 			
 			if(confirm("수정한 내용으로 게시물을 등록하시겠습니까?") == true)
 			{
 				document.updateForm.submit();
 			}
 		});
 		
 		$("#btnList").on("click", function(){
 			if(confirm("게시물 수정을 중단하고 리스트로 돌아가시겠습니까?") == true)
			{
				document.bbsForm.action = "/board/list.jsp";
 				document.bbsForm.submit();
			}
 		});
<%
 	}
%>	
});

</script>
</head>
<body>
<body>
<%@ include file="/include/navigation.jsp" %>
<%
if(boa != null)
{
%>					
	<div class="container">
	   <h2>Modify Contents</h2>
	   <form name="updateForm" id="updateForm" action="/board/updateProc.jsp" method="post">
	      <input type="text" name="bbsName" id="bbsName" maxlength="20" value="<%=boa.getMemName()%>" style="ime-mode:active;" value="이름" class="form-control mt-4 mb-2" placeholder="이름을 입력해주세요." readonly />
	      <input type="text" name="bbsTitle" id="bbsTitle" maxlength="100" style="ime-mode:active;" value="<%=boa.getBoaTitle()%>" class="form-control mb-2" placeholder="제목을 입력해주세요." required />
	      <div class="form-group">
	         <textarea class="form-control" rows="10" name="bbsContent" id="bbsContent" style="ime-mode:active;" placeholder="내용을 입력해주세요" required><%=boa.getBoaContent()%></textarea>
	      </div>
	      <input type="hidden" name="bbsSeq" value="<%=boaNum%>"/>
	      <input type="hidden" name="searchType" value="<%=searchType%> " />
	      <input type="hidden" name="searchValue" value="<%=searchValue%> " />
	      <input type="hidden" name="curPage" value="<%=curPage%> " />
	   </form>
	   
	   <div class="form-group row" >
	      <div class="col-sm-12" style="float:right">
	         <button type="button" id="btnUpdate" class="btn btn-primary" title="수정">수정</button>
	         <button type="button" id="btnList" class="btn btn-secondary" title="리스트">리스트</button>
	      </div>
	   </div>
	</div>
	

	<form name="bbsForm" id = "bbsForm" method="post" >
		  <input type="hidden" name="bbsSeq" value="<%=boaNum%>"/>
	      <input type="hidden" name="searchType" value="<%=searchType%> " />
	      <input type="hidden" name="searchValue" value="<%=searchValue%> " />
	      <input type="hidden" name="curPage" value="<%=curPage%> " />
	</form>
<%
}
%>
</body>
</html>