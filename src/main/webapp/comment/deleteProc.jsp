<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import= "java.util.List"%>
<%@ page import="org.apache.logging.log4j.LogManager" %>
<%@ page import="org.apache.logging.log4j.Logger" %>
<%@ page import="com.sist.web.util.HttpUtil" %>
<%@ page import="com.sist.web.util.CookieUtil" %>
<%@ page import="com.sist.web.proj.Board" %>
<%@ page import="com.sist.web.proj.Member" %>
<%@ page import="com.sist.web.proj.Comment" %>
<%@ page import="com.sist.web.projdao.BoardDao" %>
<%@ page import="com.sist.web.projdao.MemberDao" %>
<%@ page import="com.sist.web.projdao.CommentDao" %>
<%@ page import="com.sist.common.util.StringUtil" %>
<%
	Logger logger = LogManager.getLogger("/comment/deleteProc.jsp");
	HttpUtil.requestLogString(request, logger);
	
	String msg = "";
	
	String cookieUserId = CookieUtil.getValue(request, "MEM_ID");
	MemberDao memdao = new MemberDao();
	Member mem = memdao.selectMem(cookieUserId);
	
	long commNum = HttpUtil.get(request, "commNum", (long)0);
	
	long boaNum = HttpUtil.get(request, "bbsSeq", (long)0);
	String searchType = HttpUtil.get(request, "searchType", "");
	String searchValue = HttpUtil.get(request, "searchValue", "");
	long curPage = HttpUtil.get(request, "curPage", (long)1);
	
	if(!StringUtil.isEmpty(cookieUserId) & !StringUtil.isEmpty(boaNum))
	{
		CommentDao commdao = new CommentDao();
		if(commdao.deleteComment(commNum) > 0)
		{
			msg = "댓글이 삭제되었습니다.";
		}
		else
		{
			msg = "댓글 삭제 중 오류가 발생했습니다.";
	
		}
	}
	else
	{
		msg = "로그인 후 이용 가능합니다.";
	}
%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/include/head.jsp" %>
<script>
$(document).ready(function(){
	alert("<%=msg%>");
	document.bbsForm.action = "/board/view.jsp";
	document.bbsForm.submit();
});
</script>
</head>
<body>
	<form name="bbsForm" id="bbsForm" method="post">
		<input type="hidden" name="bbsSeq" value="<%=boaNum%>">
		<input type="hidden" name="searchType" value="<%=searchType%>">
		<input type="hidden" name="searchValue" value="<%=searchValue%>">
		<input type="hidden" name="curPage" value="<%=curPage%>">
	</form>
</body>
</html>