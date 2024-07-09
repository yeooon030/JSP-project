<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.logging.log4j.LogManager" %>
<%@ page import="org.apache.logging.log4j.Logger" %>
<%@ page import="com.sist.web.util.HttpUtil" %>
<%@ page import="com.sist.web.util.CookieUtil" %>
<%@ page import="com.sist.web.projdao.BoardDao" %>
<%@ page import="com.sist.web.proj.Board" %> 
<%@ page import="com.sist.common.util.StringUtil" %>
<%
	Logger logger = LogManager.getLogger("/board/updateProc.jsp");
	HttpUtil.requestLogString(request, logger);
	
	String cookieUserId = CookieUtil.getValue(request, "MEM_ID");
	
	long boaNum = HttpUtil.get(request, "bbsSeq", (long)0);
	String searchType = HttpUtil.get(request, "searchType", "");
	String searchValue = HttpUtil.get(request, "searchValue", "");
	long curPage = HttpUtil.get(request, "curPage", (long)1);
	
	String boaTitle = HttpUtil.get(request, "bbsTitle", "");
	String boaContent = HttpUtil.get(request, "bbsContent", "");
	
	boolean bSuccess = false;
	String msg = "";
	
	if(boaNum > 0 && !StringUtil.isEmpty(boaTitle) && !StringUtil.isEmpty(boaContent))
	{
		BoardDao boadao = new BoardDao();
		Board boa = boadao.viewBoa(boaNum);
		
		if(boa != null)
		{
			if(StringUtil.equals(cookieUserId, boa.getMemId()))
			{
				boa.setBoaNum(boaNum);
				boa.setBoaTitle(boaTitle);
				boa.setBoaContent(boaContent);
				
				if(boadao.updateBoa(boa) > 0)
				{
					bSuccess = true;
				}
				else
				{
					msg = "게시물 수정 중 오류가 발생했습니다.";
				}
					
			}
			else
			{
				msg = "사용자 정보가 일치하지 않습니다.";
			}
		}
		else
		{
			msg = "게시물이 존재하지 않습니다.";
		}
	}
	else
	{
		msg = "게시물 수정 값이 올바르지 않습니다.";
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
		alert("게시물이 수정되었습니다.");
		document.bbsForm.action = "/board/view.jsp";
		document.bbsForm.submit();
<%
	}
	else
	{
%>
		alert("<%=msg%>");
		location.href = "/board/list.jsp";
<%
	}
%>
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