<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import= "java.util.List"%>
<%@ page import="org.apache.logging.log4j.LogManager" %>
<%@ page import="org.apache.logging.log4j.Logger" %>
<%@ page import="com.sist.web.util.HttpUtil" %>
<%@ page import="com.sist.web.util.CookieUtil" %>
<%@ page import="com.sist.web.projdao.MemberDao" %>
<%@ page import="com.sist.web.proj.Member" %>
<%@ page import="com.sist.common.util.StringUtil" %>
<%
Logger logger = LogManager.getLogger("write.jsp");
	HttpUtil.requestLogString(request, logger);
	
	String cookieUserId = CookieUtil.getValue(request, "MEM_ID");
	
	String searchType = HttpUtil.get(request, "searchType", "");
	String searchValue = HttpUtil.get(request, "searchValue", "");
	long curPage = HttpUtil.get(request, "curPage", (long)1);
	
	MemberDao memDao = new MemberDao();
	Member mem = memDao.selectMem(cookieUserId);
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
	$("#btnList").on("click", function(){
		if(confirm("게시글 작성을 중단하고 리스트로 돌아가시겠습니까?")== true)
		{
			document.bbsForm.action = "/board/list.jsp";
			document.bbsForm.submit();
		}
	});
	
	$("#btnWrite").on("click", function(){
		if($.trim($("#bbsTitle").val()) <= 0)
		{
			alert("제목을 입력해주세요.");
			$("#bbsTitle").focus();
			return;
		}
		
		if($.trim($("#bbsContent").val()) <= 0)
		{
			alert("내용을 입력해주세요.");
			$("#bbsContent").focus();
			return;
		}
		
		document.writeForm.submit();
	});
});
</script>
</head>
<body>
<%@ include file="/include/navigation.jsp" %>
<div class="container">
	<div class="boa-title">
         <h2>WRITE</h2>
	</div>
   <form name="writeForm" id="writeForm" action="/board/writeProc.jsp" method="POST">
      <input type="text" name="bbsName" id="bbsName" maxlength="20" value="<%=mem.getMemName()%>" style="ime-mode:active;" class="form-control mt-4 mb-2" placeholder="이름을 입력해주세요." readonly />
      <input type="text" name="bbsTitle" id="bbsTitle" maxlength="100" style="ime-mode:active;" class="form-control mb-2" placeholder="제목을 입력해주세요." required />
      <div class="form-group">
         <textarea class="form-control" rows="10" name="bbsContent" id="bbsContent" style="ime-mode:active;" placeholder="내용을 입력해주세요" required></textarea>
      </div>

      <div class="form-group row">
         <div class="col-sm-12">
            <button type="button" id="btnWrite" name="btnWrite" class="btn btn-secondary mb-3">저장</button>
            <button type="button" id="btnList" name="btnList" class="btn btn-secondary mb-3" >리스트</button>
         </div>
      </div>
   </form>
	
	<form name="bbsForm" id="bbsForm" method="POST">
		<input type="hidden" name="searchType" value="<%=searchType%>">
		<input type="hidden" name="searchValue" value="<%=searchValue%>">
		<input type="hidden" name="curPage" value="<%=curPage %>">
	</form>
</div>

</body>
</html>