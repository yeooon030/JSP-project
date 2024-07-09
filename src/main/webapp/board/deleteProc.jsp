<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.logging.log4j.LogManager" %>
<%@ page import="org.apache.logging.log4j.Logger" %>
<%@ page import="com.sist.web.util.HttpUtil" %>
<%@ page import="com.sist.web.util.CookieUtil" %>
<%@ page import="com.sist.web.projdao.BoardDao" %>
<%@ page import="com.sist.web.projdao.CommentDao" %>
<%@ page import="com.sist.web.proj.Board" %>
<%@ page import="com.sist.common.util.StringUtil" %>
<%
	Logger logger = LogManager.getLogger("/board/deleteProc.jsp");
	HttpUtil.requestLogString(request, logger);
	
	String cookieUserId = CookieUtil.getValue(request, "MEM_ID");
	
	boolean bSuccess = false;
	String msg = "";

	long boaNum = HttpUtil.get(request, "bbsSeq", (long)0);
	
	if(boaNum > 0)
	{
		BoardDao boadao = new BoardDao();
		Board boa = boadao.viewBoa(boaNum);
		if(boa != null)
		{
			if(StringUtil.equals(cookieUserId, boa.getMemId()) || StringUtil.equals(cookieUserId, "admin"))
			{
				CommentDao commdao = new CommentDao();
				if(boadao.deleteBoa(boaNum) > 0)
				{
					if(commdao.countComm(boaNum) > 0){
						commdao.deleteAllComment(boaNum);
					}
					bSuccess = true;
					msg = "게시물이 삭제되었습니다.";
				}
				else
				{
					msg = "게시물 삭제 중 오류가 발생했습니다.";
				}
			}
			else 
			{
				msg = "로그인 사용자의 게시물이 아닙니다.";
			}
		}
		else
		{
			msg = "존재하지 않는 게시물입니다.";
		}
	}
	else
	{
		msg = "게시물 정보가 올바르지 않습니다.";
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
		alert("게시물이 성공적으로 삭제되었습니다.");
<%
	}
	else
	{
%>
		alert("<%=msg%>");
<%
	}
%>	
		location.href = "/board/list.jsp";
});
</script>
</head>
<body>

</body>
</html>