<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.logging.log4j.LogManager" %>
<%@ page import="org.apache.logging.log4j.Logger" %>
<%@ page import="com.sist.web.util.HttpUtil" %>
<%@ page import="com.sist.web.util.CookieUtil" %>
<%@ page import="com.sist.web.projdao.MemberDao" %>
<%@ page import="com.sist.web.proj.Member" %> 
<%@ page import="com.sist.common.util.StringUtil" %>
<%
	Logger logger = LogManager.getLogger("memProc.jsp");
	HttpUtil.requestLogString(request, logger);
	
	String msg = "";
	String redirectUrl = "";
	
	String cookieUserId = CookieUtil.getValue(request, "MEM_ID");
	
	MemberDao memDao = new MemberDao();
	Member mem = null;
	
	//회원가입
	if(StringUtil.isEmpty(cookieUserId))
	{
		String memId = HttpUtil.get(request, "regId");
		String memPwd = HttpUtil.get(request, "regPwd");
		String memName = HttpUtil.get(request, "regName");
		String memEmail = HttpUtil.get(request, "regEmail");
		
		if(!StringUtil.isEmpty(memId) && !StringUtil.isEmpty(memPwd) &&
		!StringUtil.isEmpty(memName) && !StringUtil.isEmpty(memEmail))
		{
	if(memDao.checkMemId(memId) > 0)
	{
		msg = "이미 가입된 아이디입니다.";
		redirectUrl = "/";
	}
	else
	{
		mem = new Member();
		
		mem.setMemId(memId);
		mem.setMemPwd(memPwd);
		mem.setMemName(memName);
		mem.setMemEmail(memEmail);
		mem.setMemSta("Y");
		
		if(memDao.insertMem(mem) > 0)
		{
	msg = "회원가입에 성공했습니다.";
	redirectUrl = "/board/list.jsp";
		}
		else
		{
	msg = "회원가입에 실패했습니다.";
	redirectUrl = "/index.jsp";
		}
	}
		}
		else
		{
	msg = "아이디나 비밀번호가 입력되지 않았습니다.";
	redirectUrl = "/index.jsp";
		}
	}
	else		//회원정보 수정(쿠키ㅇ)
	{
		mem = memDao.selectMem(cookieUserId);
		
		if(mem != null)
		{
	String memId = HttpUtil.get(request, "memId");
	String memPwd = HttpUtil.get(request, "memPwd");
	String memName = HttpUtil.get(request, "memName");
	String memEmail = HttpUtil.get(request, "memEmail");
	
	if(!StringUtil.equals(mem.getMemSta(), "Y"))
	{
		CookieUtil.deleteCookie(request, response, "/", "MEM_ID");
		msg = "정지된 사용자입니다.";
		redirectUrl = "/index.jsp";
		mem = null;
	}
	else
	{
		if(!StringUtil.isEmpty(memId) && !StringUtil.isEmpty(memPwd) &&
		!StringUtil.isEmpty(memName) && !StringUtil.isEmpty(memEmail))
		{
	mem.setMemId(memId);
	mem.setMemPwd(memPwd);
	mem.setMemName(memName);
	mem.setMemEmail(memEmail);
	
	
	if(memDao.updateMem(mem) > 0)
	{
		msg = "회원정보가 수정되었습니다";
		redirectUrl = "/member/updateForm.jsp";
		logger.debug(memDao.updateMem(mem));
		
	}
	else
	{
		msg = "회원 정보 수정 중 오류가 발생했습니다.";
		redirectUrl = "/member/updateForm.jsp";
		mem = null;
	}
		}
		else
		{
	msg = "입력값이 올바르지 않습니다.";
	redirectUrl = "/member/updateForm.jsp";
	mem = null;
		}
	}
		}
		else
		{
	CookieUtil.deleteCookie(request, response, "/", "MEM_ID");
	msg = "사용자 정보가 올바르지 않습니다";
	redirectUrl = "/index.jsp";
	mem = null;
		}
	}
%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/include/head.jsp" %>
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