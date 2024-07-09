<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.logging.log4j.LogManager" %>
<%@ page import="org.apache.logging.log4j.Logger" %>
<%@ page import="com.sist.web.util.HttpUtil" %>
<%@ page import="com.sist.web.util.CookieUtil" %>
<%@ page import="com.sist.web.projdao.MemberDao" %>
<%@ page import="com.sist.web.proj.Member" %> 
<%@ page import="com.sist.common.util.StringUtil" %>
<%
Logger logger = LogManager.getLogger("deleteMem.jsp");
	HttpUtil.requestLogString(request, logger);
	
	String memId = HttpUtil.get(request, "memId");
	String cookieUserId = CookieUtil.getValue(request, "MEM_ID");
	
	String msg = "";
	String redirectUrl = "";
	
	if(!StringUtil.isEmpty(memId))
	{
		MemberDao memDao = new MemberDao();
		Member mem = null;
		if(memDao.deleteMem(memId) > 0)
		{
	CookieUtil.deleteCookie(request, response, "/", "MEM_ID");
	msg = "회원 탈퇴가 처리되었습니다. 감사합니다.";
	redirectUrl = "/index.jsp";
		}
		else
		{
	msg = "회원 탈퇴 처리 중 오류가 발생했습니다. 다시 시도해주세요.";
	redirectUrl = "/member/updateForm.jsp";
		}
	}
	else
	{
		msg = "처리 중 오류가 발생했습니다.";
		redirectUrl = "/member/updateForm.jsp";
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