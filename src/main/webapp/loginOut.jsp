<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.logging.log4j.LogManager" %>
<%@ page import="org.apache.logging.log4j.Logger" %>
<%@ page import="com.sist.web.util.HttpUtil" %>
<%@ page import="com.sist.web.util.CookieUtil" %>
<%
	Logger logger = LogManager.getLogger("loginOut.jsp");
	HttpUtil.requestLogString(request, logger);
	
	String cookieUserId = CookieUtil.getValue(request, "MEM_ID");
	if(cookieUserId != null)
	{
		CookieUtil.deleteCookie(request, response, "/", "MEM_ID");
		response.sendRedirect("/");
	}
	
%>
