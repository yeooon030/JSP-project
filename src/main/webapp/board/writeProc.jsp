<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import= "java.util.List"%>
<%@ page import="org.apache.logging.log4j.LogManager" %>
<%@ page import="org.apache.logging.log4j.Logger" %>
<%@ page import="com.sist.web.util.HttpUtil" %>
<%@ page import="com.sist.web.util.CookieUtil" %>
<%@ page import="com.sist.web.proj.Board" %>
<%@ page import="com.sist.web.projdao.BoardDao" %>
<%@ page import="com.sist.common.util.StringUtil" %>
<%
	Logger logger = LogManager.getLogger("/board/writeProc.jsp");
	HttpUtil.requestLogString(request, logger);
	
	String cookieUserId = CookieUtil.getValue(request, "MEM_ID");
	
	String memName = HttpUtil.get(request, "memName");
	String boaTitle = HttpUtil.get(request, "bbsTitle");
	String boaContent = HttpUtil.get(request, "bbsContent");
	
	boolean bSuccess = false;
	String msg = "";
	
	if(!StringUtil.isEmpty(boaTitle) && !StringUtil.isEmpty(boaContent))
	{
		BoardDao boadao = new BoardDao();
		Board boa = new Board();
		
		boa.setMemId(cookieUserId);
		boa.setMemName(memName);
		boa.setBoaTitle(boaTitle);
		boa.setBoaContent(boaContent);
		
		if(boadao.uploadBoa(boa) > 0)
		{
			bSuccess = true;
		}
		else
		{
			msg = "게시물 등록 중 오류가 발생했습니다.";
		}
	}
	else
	{
		msg = "게시물의 제목이나 내용이 입력되지 않았습니다.";
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
		alert = "게시물이 성공적으로 등록되었습니다.";
		location.href = "/board/list.jsp";
		
<%
	}
	else
	{
%>
		alert = "<%=msg%>";
		location.href = "/board/write.jsp";
<%
	}
%>
});
</script>
</head>
<body>

</body>
</html>