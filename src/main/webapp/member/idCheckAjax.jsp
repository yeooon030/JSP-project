<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.logging.log4j.LogManager" %>
<%@ page import="org.apache.logging.log4j.Logger" %>
<%@ page import="com.sist.web.util.HttpUtil" %>
<%@ page import="com.sist.web.util.CookieUtil" %>
<%@ page import="com.sist.web.projdao.MemberDao" %>
<%@ page import="com.sist.web.proj.Member" %>
<%@ page import="com.sist.common.util.StringUtil" %>
<%
Logger logger = LogManager.getLogger("idCheckAjax.jsp");
	HttpUtil.requestLogString(request, logger);
	
	String regId = HttpUtil.get(request, "regId");
	logger.debug("[Ajax]regId : " + regId);
	
	if(!StringUtil.isEmpty(regId))
	{
		MemberDao memDao = new MemberDao();
		
		if(memDao.checkMemId(regId) <= 0)
		{
			response.getWriter().write("{\"flag\":0}");
		}
		else
		{	
			response.getWriter().write("{\"flag\":1}");
		}
	}
	else
	{
		response.getWriter().write("{\"flag\":-1}");
	}
%>