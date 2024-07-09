<%@page import="com.sist.web.db.DBManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.logging.log4j.LogManager" %>
<%@ page import="org.apache.logging.log4j.Logger" %>
<%@ page import="com.sist.web.util.HttpUtil" %>
<%@ page import="com.sist.web.util.CookieUtil" %>
<%@ page import="com.sist.web.projdao.MemberDao" %>
<%@ page import="com.sist.web.proj.Member" %> 
<%@ page import="com.sist.common.util.StringUtil" %>
<%
Logger logger = LogManager.getLogger("updateForm.jsp");
	DBManager.getConnection();
	
	String cookieUserId = CookieUtil.getValue(request, "MEM_ID");
	Member mem = null;
	
	if(!StringUtil.isEmpty(cookieUserId))
	{
		MemberDao memdao = new MemberDao();
		mem = memdao.selectMem(cookieUserId);
		if(mem == null)
		{
	CookieUtil.deleteCookie(request, response, "MEM_ID");
	response.sendRedirect("/index.jsp");
		}
		else
		{
	if(!StringUtil.equals(mem.getMemSta(), "Y"))
	{
		CookieUtil.deleteCookie(request, response, "MEM_ID");
		mem = null;
		response.sendRedirect("/index.jsp");
	}
		}	
	}
	
	if(mem != null)
	{
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@ include file="/include/head.jsp" %>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Bagel+Fat+One&display=swap" rel="stylesheet">
<style>
a {color:black;}
a:hover{color:lightblue;}
th{background-color: white}
h1{position:center; font-family: 'Bagel Fat One', system-ui; font-size:5rem}
a.page-link{background-color: black; color:white;}
a.page-link:hover{background-color:lightblue;color:white}
.navbar{background-color: black;}
.boa-title{ text-align: center; margin: 20px; padding:20px}
.btn {background-color: black; border-color:black;}
.btn:hover{background-color:lightblue; border-color: white;}
.navba{background-color: black;}
</style>
<script>
$(document).ready(function(){
	$("#btnUpdate").on("click", function(){
		var idPwCheck = /^[a-zA-Z0-9]{4,12}$/;
		var emptCheck = /\s/g;
		
		//비밀번호 체크
		if($.trim($("#memPwd1").val()).length <= 0)
		{
			alert("비밀번호를 입력하세요.");
			$("#memPwd1").val("");
			$("#memPwd1").focus();
			return;
		}
		
		if(emptCheck.test($("#memPwd1").val()))
		{
			alert("비밀번호는 공백 없이 입력해주세요.");
			$("#memPwd1").focus();
			return;
		}
		
		if(!idPwCheck.test($("#memPwd1").val()))
		{
			alert("비밀번호는 4~12자리 영문자, 숫자만 입력할 수 있습니다.");
			$("#memPwd1").focus();
			return;
		}
		
		if($("#memPwd1").val() != $("#memPwd2").val())
		{
			alert("비밀번호가 일치하지 않습니다.");
			$("#memPwd2").focus();
			return;
		}
		
		$("#memPwd").val($("#memPwd1").val());
		
		//이름체크
		if($.trim($("#memName").val()).length <= 0)
		{
			alert("이름을 입력하세요.");
			$("#memName").val("");
			$("#memName").focus();
			return;
		}
		
		if(emptCheck.test($("#memName").val()))
		{
			alert("이름은 공백 없이 입력해주세요.");
			$("#memName").focus();
			return;
		}
		
		if(($("#memName").val()).length <= 1)
		{
			alert("이름을 정확하게 입력해주세요.");
			$("#memName").focus();
			return;
		}
		
		//이메일 체크
		if($.trim($("#memEmail").val()).length <= 0)
		{
			alert("이메일을 입력하세요.");
			$("#memEmail").val("");
			$("#memEmail").focus();
			return;
		}
		
		if(!fn_validateEmail($("#memEmail").val()))
		{
			alert("이메일을 정확하게 입력해주세요.");
			$("#memEmail").focus();
			return;
		}
		
		document.updateForm.submit();
	});
	
	$("#btnWithdrawl").on("click", function(){
		if(confirm("회원 탈퇴 시 게시물에 접근할 수 없습니다. 탈퇴하시겠습니까?") == true)
		{
			document.withdrawalForm.submit();
		}
	});
});

function fn_validateEmail(value)
{
   var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
   
   return emailReg.test(value);
}

</script>
</head>
<body>
<%@ include file="/include/navigation.jsp" %>
<div class="container">
    <div class="boa-title" style="position:center;">
       <h1 class="boa-title">Update form</h1>
    </div>
    <div class="row mt-2">
        <div class="col-12">
            <form name="updateForm" id="updateForm" action="/member/memProc.jsp" method="post">
                <div class="form-group">
                    <b><label for="memName">사용자 아이디 : </label></b>
                    <%=mem.getMemId()%>
                </div>
                <div class="form-group">
                    <label for="memName">비밀번호</label>
                    <input type="password" class="form-control" id="memPwd1" name="memPwd1" value="<%=mem.getMemPwd()%>" placeholder="비밀번호" maxlength="12" />
                </div>
                <div class="form-group">
                    <label for="memName">비밀번호 확인</label>
                    <input type="password" class="form-control" id="memPwd2" name="memPwd2" value="<%=mem.getMemPwd()%>" placeholder="비밀번호 확인" maxlength="12" />
                </div>
                <div class="form-group">
                    <label for="memName">사용자 이름</label>
                    <input type="text" class="form-control" id="memName" name="memName" value="<%=mem.getMemName()%>" placeholder="사용자 이름" maxlength="15" />
                </div>
                <div class="form-group">
                    <label for="memName">사용자 이메일</label>
                    <input type="text" class="form-control" id="memEmail" name="memEmail" value="<%=mem.getMemEmail()%>" placeholder="사용자 이메일" maxlength="30" />
                </div>
				<input type="hidden" id="memId" name = "memId" value="<%=mem.getMemId()%>">
				<input type="hidden" id="memPwd" name="memPwd" value="">
                <button type="button" id="btnUpdate" name="btnUpdate" class="btn btn-primary">수정</button>
                <button type="button" id="btnWithdrawl" name="btnWithdrawl" class="btn btn-primary">탈퇴</button>
            </form>
        </div>
    </div>
</div>
<form name="withdrawalForm" id="withdrawalForm" method = "post" action = "/member/deleteMem.jsp" >
	<input type="hidden" id="memId" name = "memId" value="<%=mem.getMemId()%>">
</form>
</body>
<%
	}
%>
</html>
