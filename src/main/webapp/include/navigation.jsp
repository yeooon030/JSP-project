<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.sist.common.util.StringUtil" %>
<%@ page import="com.sist.web.util.CookieUtil" %>
<%@ page import="com.sist.web.projdao.MemberDao" %>
<%@ page import="com.sist.web.proj.Member" %>
<%
	//String cookieUserId = CookieUtil.getValue(request, "MEM_ID");
	//MemDao memdao = new MemDao();
	//Mem mem = memdao.selectMem(cookieUserId);

	if(StringUtil.isEmpty(CookieUtil.getValue(request, "MEM_ID")))
	{
		
%>
<nav class="navbar navbar-expand-sm bg-secondary navbar-dark mb-3" style="background-color: black;"> 
	<ul class="navbar-nav"> 
	    <li class="nav-item"> 
	      <a class="nav-link" href="/" style="color:white;"> 로그인</a> 
	    </li> 
	    <li class="nav-item"> 
	      <a class="nav-link" href="/" style="color:white;">회원가입</a> 
	    </li> 
  </ul>
  <ul>
  		<li>
  </ul>
</nav>
<%
	}
	else if(StringUtil.equals(CookieUtil.getValue(request, "MEM_ID"), "admin"))
	{
%>
<nav class="navbar navbar-expand-sm navbar-dark mb-3" style="background-color: black;"> 
	<ul class="navbar-nav"> 
   	    <li class="nav-item"> 
	      <a class="nav-link" href="/loginOut.jsp" style="color:white;"> 로그아웃</a> 
	    </li> 
	    <li class="nav-item"> 
	      <a class="nav-link" href="/board/memList.jsp" style="color:white;">회원관리</a> 
	    </li> 
	    <li class="nav-item"> 
	      <a class="nav-link" href="/board/list.jsp" style="color:white;">게시판</a> 
	    </li>
  	</ul>

</nav>
<%
	}
	else
	{
%>
<nav class="navbar navbar-expand-sm  navbar-dark mb-3" style="background-color: black;"> 
	<ul class="navbar-nav"> 
	    <li class="nav-item"> 
	      <a class="nav-link" href="/loginOut.jsp" style="color:white;"> 로그아웃</a> 
	    </li> 
	    <li class="nav-item"> 
	      <a class="nav-link" href="/member/updateForm.jsp" style="color:white;">회원정보 수정</a> 
	    </li> 
	    <li class="nav-item"> 
	      <a class="nav-link" href="/board/list.jsp" style="color:white;">게시판</a> 
	    </li>
 	 </ul> 
</nav>
<%
	}
%>