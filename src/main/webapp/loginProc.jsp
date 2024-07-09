<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.logging.log4j.LogManager" %>
<%@ page import="org.apache.logging.log4j.Logger" %>
<%@ page import="com.sist.web.util.HttpUtil" %>
<%@ page import="com.sist.web.util.CookieUtil" %>
<%@ page import="com.sist.web.projdao.MemberDao" %>      
<%@ page import="com.sist.web.proj.Member" %>
<%@ page import="com.sist.common.util.StringUtil" %>

<%
Logger logger = LogManager.getLogger("loginProc.jsp");
	HttpUtil.requestLogString(request, logger);
	
	String memId = HttpUtil.get(request, "memId");	//request.getParameter("memId");
	String memPwd = HttpUtil.get(request, "memPwd");	//request.getParameter("memPwd");
	String cookieMemId = CookieUtil.getValue(request, "MEM_ID");
	
	logger.debug("memId : " + memId);
	logger.debug("memPwd : " + memPwd);
	logger.debug("cookieMemId : " + cookieMemId);
	
	String msg = "";
	String redirectUrl = "";
	
	Member mem = null;
	MemberDao memDao = new MemberDao();
	
	if(StringUtil.isEmpty(cookieMemId))
	{
		if(!StringUtil.isEmpty(memId) && !StringUtil.isEmpty(memPwd))
		{	
	mem = memDao.selectMem(memId);
	
	if(mem != null)
	{
		if(StringUtil.equals(mem.getMemSta(), "X"))
		{
	msg = "사용할 수 없는 계정입니다.";
	redirectUrl = "/";
		}
		else if(StringUtil.equals(memPwd, mem.getMemPwd()))
		{
	if(StringUtil.equals(mem.getMemSta(), "Y"))
	{
		CookieUtil.addCookie(response, "/", "MEM_ID", memId);
		msg = "로그인에 성공했습니다.";
		//response.sendRedirect("/board/list.jsp");
		redirectUrl = "/board/list.jsp";
	}
	else
	{
		msg = "정지된 계정입니다.";
		redirectUrl = "/";
	}
		}
		else
		{
	msg = "비밀번호가 일치하지 않습니다.";
	redirectUrl = "/";
		}
	}
	else
	{
		msg = "존재하지 않는 아이디입니다.";
		redirectUrl = "/";
	}
		}
		else
		{
	msg = "아이디 혹은 비밀번호가 입력되지 않았습니다.";
	redirectUrl = "/";
		}
	}
	else
	{
		if(!StringUtil.isEmpty(memId) && !StringUtil.isEmpty(memPwd))
		{
	mem = memDao.selectMem(memId);
	
	if(mem != null)
	{
		if(StringUtil.equals(memPwd, mem.getMemPwd()))
		{
	if(StringUtil.equals(mem.getMemSta(), "Y"))
	{
		if(!StringUtil.equals(cookieMemId, memId))
		{
			CookieUtil.deleteCookie(request, response, "/", "MEM_ID");
			CookieUtil.addCookie(response, "MEM_ID", memId);
		}

		msg = "환영합니다.";
		redirectUrl = "/board/list.jsp";
	}
	else if(StringUtil.equals(mem.getMemSta(), "N"))
	{
		CookieUtil.deleteCookie(request, response, "/", "MEM_ID");
		msg = "사용이 정지된 회원입니다. 관리자에게 문의바랍니다.";
		redirectUrl = "/";
	}
	else
	{
		CookieUtil.deleteCookie(request, response, "/", "MEM_ID");
		msg = "사용할 수 없는 아이디입니다.";
		redirectUrl = "/";
	}
		}
		else
		{
	CookieUtil.deleteCookie(request, response, "/", "MEM_ID");
	msg = "비밀번호가 올바르게 입력되지 않았습니다.";
	redirectUrl = "/";
		}
	}
	else
	{
		CookieUtil.deleteCookie(request, response, "/", "MEM_ID");
		msg = "존재하지 않는 계정입니다.";
		redirectUrl = "/";
	}
		}
		else
		{
	CookieUtil.deleteCookie(request, response, "/", "MEM_ID");
	msg = "아이디 혹은 비밀번호를 입력해주세요.";
	redirectUrl = "/";
		}
	}
%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/include/head.jsp" %>
<link rel="stylesheet" href="resources/css/test.css" type="text/css" />
<script>
$(document).ready(function(){
	alert("<%=msg%>");
	location.href = "<%=redirectUrl%>";
});
</script>
</head>
<body>

</body>
</html>