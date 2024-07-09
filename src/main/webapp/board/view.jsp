<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.logging.log4j.LogManager" %>
<%@ page import="org.apache.logging.log4j.Logger" %>
<%@ page import="com.sist.web.util.HttpUtil" %>
<%@ page import="com.sist.web.util.CookieUtil" %>
<%@ page import="com.sist.web.projdao.BoardDao" %>
<%@ page import="com.sist.web.projdao.CommentDao" %>
<%@ page import="com.sist.web.proj.Board" %>
<%@ page import="com.sist.web.proj.Comment" %>
<%@ page import="com.sist.common.util.StringUtil" %>
<%@ page import= "java.util.List"%>

<%
	Logger logger = LogManager.getLogger("/board/view.jsp");
	HttpUtil.requestLogString(request, logger);
	
	String cookieUserId = CookieUtil.getValue(request, "MEM_ID");
	long boaNum = HttpUtil.get(request, "bbsSeq", (long)0);
	String searchType = HttpUtil.get(request, "searchType", "");
	String searchValue = HttpUtil.get(request, "searchValue", "");
	long curPage = HttpUtil.get(request, "curPage", (long)1);
	
	BoardDao boaDao = new BoardDao();
	Board boa = boaDao.viewBoa(boaNum);

	CommentDao commdao = new CommentDao();
	List<Comment> list = commdao.boardList(boaNum);
	
	if(boa != null)
	{
		boaDao.readCntBoa(boaNum);
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

<script>
$(document).ready(function(){
<%if(boa == null)
	{%>
		alert("조회한 게시물이 존재하지 않습니다.");
		document.bbsForm.action("/board/list.jsp");
		document.bbsForm.submit();
<%	}
	else
	{%>
		$("#comm_write").focus();
  		
		$("#comm_btn").on("click", function(){
			if($.trim($("#comm_write").val()).length <= 0)
			{
				alert("댓글 내용을 입력해주세요.");
				$("#comm_btn").val("");
				$("#comm_btn").focus();
			}
			else
			{
				if(confirm("댓글을 입력하시겠습니까?") == true)
				{
	  				document.commentForm.submit();
				}
			}
		});
		
  		$("#btnList").on("click", function(){
  			document.bbsForm.action = "/board/list.jsp";
  			document.bbsForm.submit();
  		});
  		
  		$("#btnlike").on("click", function(){

  			if(confirm("추천하시겠습니까?") == true)
  			{
  				document.bbsForm.action = "/board/likeProc.jsp";
  				document.bbsForm.submit();
  			}

  		});

		$("#btnUpdate").on("click", function(){
			document.bbsForm.action = "/board/update.jsp";
			document.bbsForm.submit();
		});
			
		$("#btnDelete").on("click", function(){
			if(confirm("게시물을 삭제하시겠습니까?") == true)
			{
				document.bbsForm.action = "/board/deleteProc.jsp";
				document.bbsForm.submit();
				}
		});
	
<%	}%>
});
function deleteComment(commNum){
	if(confirm("해당 댓글을 삭제하시겠습니까?") == true){
		document.getElementById('commNum').value = commNum;
		document.bbsForm.action = "/comment/deleteProc.jsp";
		document.bbsForm.submit();
	}
}
</script>
<style>
a {color:black;}
a:hover{color:lightblue;}
th{background-color: white}
h2{position:center; font-family: 'Bagel Fat One', system-ui; font-size:5rem}
a.page-link{background-color: black; color:white;}
a.page-link:hover{background-color:lightblue;color:white}
#comm_btn{background-color: black; color:white: boarder-radius;}
.navbar{background-color: black;}
.boa-title{width:100%;text-align: center;margin: 20px;padding:20px}
.btn {background-color: black;}
.btn-like{background-color: white;}
.btn:hover{background-color:lightblue; border-color: white;}
.navba{background-color: black;}
.comm_form{transform:;}
.board-btn{margin-top:0;}
.comm-font{font-family: 'Bagel Fat One', system-ui;}
   @font-face {
     font-family: 'Material Icons';
     font-style: normal;
     font-weight: 400;
     src: url(https://example.com/MaterialIcons-Regular.eot); /* For IE6-8 */
     src: local('Material Icons'),
       local('MaterialIcons-Regular'),
       url(https://example.com/MaterialIcons-Regular.woff2) format('woff2'),
       url(https://example.com/MaterialIcons-Regular.woff) format('woff'),
       url(https://example.com/MaterialIcons-Regular.ttf) format('truetype');
   }
   
   .material-icons {
     font-family: 'Material Icons';
     font-weight: normal;
     font-style: normal;
     font-size: 24px;  /* Preferred icon size */
     display: inline-block;
     line-height: 1;
     text-transform: none;
     letter-spacing: normal;
     word-wrap: normal;
     white-space: nowrap;
     direction: ltr;
   
     /* Support for all WebKit browsers. */
     -webkit-font-smoothing: antialiased;
     /* Support for Safari and Chrome. */
     text-rendering: optimizeLegibility;
   
     /* Support for Firefox. */
     -moz-osx-font-smoothing: grayscale;
   
     /* Support for IE. */
     font-feature-settings: 'liga';
   }
   
   /* Rules for sizing the icon. */
   .material-icons.md-18 { font-size: 18px; }
   .material-icons.md-24 { font-size: 24px; }
   .material-icons.md-36 { font-size: 36px; }
   .material-icons.md-48 { font-size: 48px; }
   
   /* Rules for using icons as black on a light background. */
   .material-icons.md-dark { color: rgba(0, 0, 0, 0.54); }
   .material-icons.md-dark.md-inactive { color: rgba(0, 0, 0, 0.26); }
   
   /* Rules for using icons as white on a dark background. */
   .material-icons.md-light { color: rgba(255, 255, 255, 1); }
   .material-icons.md-light.md-inactive { color: lightblue; }
</style>
</head>
<body>
<%
if(boa != null)
{
%>
<%@ include file="/include/navigation.jsp" %>
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
<div class="container">
   <h2>BOARD</h2>
   <div class="row" style="margin-right:0; margin-left:0;">
      <table class="table">
         <thead style="background-color:white;">
            <tr class="table-active" >
               <th scope="col" style="width:60%; background-color:white;" >
                  <%=boa.getBoaTitle()%><br/>
                  작성자 : <%=boa.getMemName()%>&nbsp;&nbsp;&nbsp;
                     
               </th>
               <th scope="col" style="width:40%; background-color:white;" class="text-right">
                  조회 : <%=boa.getBoaCnt()%><br/>
                  <%=boa.getRegDate()%>
               </th>
            </tr>
         </thead>
         <tbody>
            <tr>
               <td colspan="2"><pre><%=boa.getBoaContent()%></pre>
               	 <div class="col text-center" style="margin-top:5rem;">
                        <button type="button" name="btnlike" id="btnlike" class="btn btn-like">
                           <span class="material-icons md-36" style="color:black">thumb_up_alt</span><br>
                           추천<br>
                           <%=boa.getBoaLike()%>
                        </button>
                  </div>
               </td>
            </tr>
         </tbody>
         <tfoot>
         	<tr>
               <td colspan="2"></td>
            </tr>
         </tfoot>
      </table>
   </div>
 
   <div class="board_btn" style="float:right; justify-content: right;">
<%
if(StringUtil.equals(cookieUserId, "admin"))	
	{
%>
	   <button type="button" id="btnList" class="btn btn-secondary">리스트</button>
	   <button type="button" id="btnDelete" class="btn btn-secondary">삭제</button>
<%
}
	else
	{
		if(!StringUtil.equals(cookieUserId, boa.getMemId()))
		{
%>   
	   	   <button type="button" id="btnList" class="btn btn-secondary">리스트</button>
<%
		}
		else
		{
%>
		   <button type="button" id="btnList" class="btn btn-secondary">리스트</button>
		   <button type="button" id="btnUpdate" class="btn btn-secondary">수정</button>
		   <button type="button" id="btnDelete" class="btn btn-secondary">삭제</button>
<%
		}
	}
%>  
	</div>
   <br/>
   <br/>
<%
}
%>
<!-- ----------------------댓글--------------------------- -->
<%
if(!StringUtil.isEmpty(cookieUserId)) //로그인했을 때만 작성 가능
{
%>
   <!-- 댓글작성부-->
   <form action ="/comment/writeProc.jsp" id="commentForm" name="commentForm" class="comm_form" method="post" style="margin-top:20px">
   		<div class="" style="position:relative; align-items:center; display:flex; justify-contents:inline-block;">
	   		<img src="/resources/images/comment_img.png" style="width:8%; display:flex; position:relative; margin-left:20px; position:center;">
			<b class="comm-font" style="font-size:30px; margin-left:0px; margin-bottom:0">COMMENTS</b>
   		</div>
		<div class="row" style=" margin-bottom:3rem; margin-left:2rem; justify-content:center;">
      		<textarea id="comm_write"  name="comm_write" class="comm_write" style="width:80%; margin-right:10px; height:4rem; border: 5px solid black; border-radius:0.5rem; padding: 10px"></textarea>
      		<button type="button" id="comm_btn" name="comm_btn" class="btn btn-secondary" style="width:10%; height:4rem; border-radius: 1rem; font-size:large">등록</button>
   		</div>
   		<input type="hidden" name="bbsSeq" value="<%=boaNum%>">
   </form>
<%
}
%>

	<!-- 댓글 목록 -->
<%
if(list != null && list.size() > 0)
	{
		for(int i = 0; i < list.size(); i++)
		{
			Comment comm = list.get(i);
%>	
  <div id="comm_list" class="cm_list" style="margin-bottom:3rem" >
 	 <table width="90%" style="margin-left:4rem">
	  	<tr>
	  		<td width="2%"><img src="/resources/images/comment_img2.png" style="width:3rem; height:3rem; align:right;"></td>
	  		<td style="font-size:18px; font-weight:bold;"><%=comm.getMemName()%></td>
	  	</tr>
	  	<tr style="margin:8px;">
	  		<td></td>
	  		<td style="margin-left:30px;"><%=comm.getCommText()%></td>
	  	</tr>
	  	<tr style="border-bottom: black solid 4px; height:3.2rem;">
	  		<td></td>
	  		<td width="80%" style="float:right;text-align:right;position:relative;top:8px;right:10px"><%=comm.getCommDate() %></td>
<%
			if(StringUtil.equals(cookieUserId, comm.getMemId()) || StringUtil.equals(cookieUserId, "admin"))
			{
%>
	  		<td width="10%">
	  			<div>
	  				<button type="button" onclick="deleteComment('<%=comm.getCommNum()%>')" class="btn" style="float:right;color:white;height:40px;border-radius:1rem;padding-bottom:20px">삭제</button>  <!-- 댓글 작성자에게만 보이게! -->
	  			</div>
	  		<td>
<%
			}
%>
	  		<td></td>
	  	</tr>
	</table>
  </div>
<%
		}
	}
%>
</div>
	<form name="bbsForm" id="bbsForm" method = "post">
		<input type="hidden" name="bbsSeq" value="<%=boaNum%>">
		<input type="hidden" name="searchType" value="<%=searchType%>">
		<input type="hidden" name="searchValue" value="<%=searchValue%>">
		<input type="hidden" name="curPage" value="<%=curPage%>">
		<input type="hidden" id="commNum" name="commNum" value="">
	</form>

</body>
</html>