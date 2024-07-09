<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<%@ include file="/include/head.jsp" %>
<link rel="stylesheet" href="resources/css/test.css" type="text/css" />
<link rel="preconnect" href="https://fonts.googleapis.com" />
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
<link href="https://fonts.googleapis.com/css2?family=Bagel+Fat+One&display=swap" rel="stylesheet" />
<script>
$(document).ready(function(){
	$("#memId").focus();
	
	$('#memPwd').on('keypress', function(e){
		  if(e.keyCode == '13'){
		  	$('#loginBtn').click();
		  }
	});

	$('#regEmail').on('keypress', function(e){
		  if(e.keyCode == '13'){
		  	$('#signUp').click();
		  }
	});
	
	$("#loginBtn").on("click", function(){
		if($.trim($("#memId").val()).length <= 0)
		{
			alert("아이디를 입력하세요.");
			$("#memId").val("");
			$("#memId").focus();
			return;
		}
		
		if($.trim($("#memPwd").val()).length <= 0)
		{
			alert("비밀번호를 입력하세요.");
			$("#memPwd").val("");
			$("#memPwd").focus();
			return;
		}
		document.loginForm.submit();
	});
	
	
	$("#signUp").on("click", function(){
		$("#regId").focus();
	});	
	
	$("#signIn").on("click", function(){
		$("#regId").focus();
	});	
	
	$("#regBtn").on("click", function(){
		
		 //영문,대소문자,숫자로만 이루어진 4~12자리 정규식
		var idPwCheck = /^[a-zA-Z0-9]{4,12}$/;
		//모든 공백 체크 정규식
		var emptCheck = /\s/g;
		
		//아이디 체크
		if($.trim($("#regId").val()).length <= 0)
		{
			alert("아이디를 입력하세요.");
			$("#regId").val("");
			$("#regId").focus();
			return;
		}
		
		if(emptCheck.test($("#regId").val()))
		{
			alert("아이디는 공백 없이 입력해주세요.");
			$("#regId").focus();
			return;
		}
		
		if(!idPwCheck.test($("#regId").val()))
		{
			alert("아이디는 4~12자리 영문자, 숫자만 입력할 수 있습니다.");
			$("#regId").focus();
			return;
		}
		
		//비밀번호 체크
		if($.trim($("#regPwd1").val()).length <= 0)
		{
			alert("비밀번호를 입력하세요.");
			$("#regPwd1").val("");
			$("#regPwd1").focus();
			return;
		}
		
		if(emptCheck.test($("#regPwd1").val()))
		{
			alert("비밀번호는 공백 없이 입력해주세요.");
			$("#regPwd1").focus();
			return;
		}
		
		if(!idPwCheck.test($("#regPwd1").val()))
		{
			alert("비밀번호는 4~12자리 영문자, 숫자만 입력할 수 있습니다.");
			$("#regPwd1").focus();
			return;
		}
		
		if($("#regPwd1").val() != $("#regPwd2").val())
		{
			alert("비밀번호가 일치하지 않습니다.");
			$("#regPwd2").val("");
			$("#regPwd2").focus();
			return;
		}
		
		$("#regPwd").val($("#regPwd1").val());
		
		//이름체크
		if($.trim($("#regName").val()).length <= 0)
		{
			alert("이름을 입력하세요.");
			$("#regName").val("");
			$("#regName").focus();
			return;
		}
		
		if(emptCheck.test($("#regName").val()))
		{
			alert("이름은 공백 없이 입력해주세요.");
			$("#regName").focus();
			return;
		}
		
		if(($("#regName").val()).length <= 1)
		{
			alert("이름을 정확하게 입력해주세요.");
			$("#regName").focus();
			return;
		}
		
		//이메일 체크
		if($.trim($("#regEmail").val()).length <= 0)
		{
			alert("이메일을 입력하세요.");
			$("#regEmail").val("");
			$("#regEmail").focus();
			return;
		}
		
		if(!fn_validateEmail($("#regEmail").val()))
		{
			alert("이메일을 정확하게 입력해주세요.");
			$("#regEmail").focus();
			return;
		}

		
		$.ajax({
			type:"post",
			url:"/member/idCheckAjax.jsp",
			data:{regId:$("#regId").val()},
			datatype:"JSON",
			success:function(obj){
				var data = JSON.parse(obj);
				
				if(data.flag == 0)
				{	
					//alert("회원가입 성공!");
					document.regForm.submit();
				}
				else if(data.flag == 1)
				{
					alert("중복된 아이디가 있습니다.");
					$("#regId").focus();
				}
				else
				{
					alert("아이디 값을 확인하세요.");
					$("#regId").focus();
				}
			},
			error:function(shr, status, error){
				alert("아이디 중복체크 오류");
			}
		});
		
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
   <div class="container" id="container">
      <div class="form-container sign-in-container">
         <form id = "loginForm" name = "loginForm" method = "POST" action="loginProc.jsp">
            <h1 class="form-title" style="font-family: 'Bagel Fat One'; font-size:3rem;">Login</h1><br>
            <input type="text" name="memId" id="memId" placeholder="아이디" />
            <input type="password" name="memPwd" id="memPwd" placeholder="비밀번호" />
            <button type="button" name="loginBtn" id="loginBtn" class="mt-5">로그인</button>
         </form>
      </div>
      
      <div class="form-container sign-up-container">
         <form id = "regForm" name = "regForm" method = "POST" action="/member/memProc.jsp">
            <h1 class="form-title" style="font-family: 'Bagel Fat One'; font-size:2rem;">Register</h1>
            <input type="text" id="regId" name="regId" placeholder="아이디" />
            <input type="password" id="regPwd1" placeholder="비밀번호" />
            <input type="password" id="regPwd2" placeholder="비밀번호 확인" />
            <input type="text" id="regName" name="regName" placeholder="이름" />
            <input type="email" id="regEmail" name = "regEmail" placeholder="이메일" /><br>
             <input type="hidden" id="regPwd" name="regPwd" value="" />
            <button type="button" name="regBtn" id="regBtn">회원가입</button>
         </form>
      </div>
      
      <div class="overlay-container">
         <div class="overlay">
            <div class="overlay-panel overlay-right">
               <h1 style="font-family: 'Bagel Fat One'; font-size:3rem;">WELCOME!</h1>
               <p>회원가입을 원하시나요?</p>
               <button type="button" class="ghost" id="signUp">회원가입</button>
            </div>
            <div class="overlay-panel overlay-left">
               <h1 style="font-family: 'Bagel Fat One'; font-size:3rem;">HELLO!</h1>
               <p>로그인을 원하시나요?</p>
               <button type="button" class="ghost" id="signIn">로그인</button>
            </div>
         </div>
      </div>
   </div>
</body>
<script type="text/javascript" src="resources/js/test.js"></script>
</html>