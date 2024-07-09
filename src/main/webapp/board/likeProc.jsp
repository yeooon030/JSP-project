<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.logging.log4j.LogManager" %>
<%@ page import="org.apache.logging.log4j.Logger" %>
<%@ page import="com.sist.web.util.HttpUtil" %>
<%@ page import="com.sist.web.util.CookieUtil" %>
<%@ page import="com.sist.web.projdao.BoardDao" %>
<%@ page import="com.sist.web.proj.Board" %> 
<%@ page import="com.sist.web.projdao.LikeDao" %>
<%@ page import="com.sist.web.proj.Like" %>
<%@ page import="com.sist.common.util.StringUtil" %>
<%
	Logger logger = LogManager.getLogger("/board/likeProc.jsp");
	HttpUtil.requestLogString(request, logger);
	
	boolean bSuccess = false;
	String msg = "";
	String redirectUrl = "";
	String cookieUserId = CookieUtil.getValue(request, "MEM_ID");
	
	long boaNum = HttpUtil.get(request, "bbsSeq", (long)0);
	String searchType = HttpUtil.get(request, "searchType", "");
	String searchValue = HttpUtil.get(request, "searchValue", "");
	long curPage = HttpUtil.get(request, "curPage", (long)1);

	BoardDao boadao = new BoardDao();
	Board boa = boadao.viewBoa(boaNum);
	
	LikeDao likedao = new LikeDao();
	
	if(boa != null)
	{
		int result1 = likedao.likeExistDao(boaNum, cookieUserId);
		if(result1 == 0)
		{
			System.out.println("likedao.likeExistDao : " + result1);
			int result2  = likedao.likeDao(boaNum, cookieUserId);
			if(result2 > 0)
			{
				System.out.println("likedao.likeDao : " + result2);
				msg = "추천되었습니다.";
				bSuccess = true;
			}
			else
			{
				msg = "추천 처리 중 오류가 발생했습니다.";
				bSuccess = true;
			}
		}
		else
		{
			msg = "이미 추천한 게시물입니다.";
			bSuccess = true;
		}
	}
	else
	{
		msg = "존재하지 않는 게시물입니다.";
	}
%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/include/head.jsp" %>
<script>
$(document).ready(function(){
<%
	if(bSuccess == true)
	{
%>
		alert("<%=msg%>");
		document.bbsForm.action = "/board/view.jsp";
		document.bbsForm.submit();
<%
	}
	else
	{
%>
		alert("<%=msg%>");
		document.bbsForm.action = "/board/list.jsp";
		document.bbsForm.submit();
<%
	}
%>
});
</script>
</head>
<body>
<form name="bbsForm" id="bbsForm" method = "post">
	<input type="hidden" name="bbsSeq" value="<%=boaNum%>">
	<input type="hidden" name="searchType" value="<%=searchType%>">
	<input type="hidden" name="searchValue" value="<%=searchValue%>">
	<input type="hidden" name="curPage" value="<%=curPage%>">
</form>
</body>
</html>