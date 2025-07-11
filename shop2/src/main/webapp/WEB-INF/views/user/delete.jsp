<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><c:catch></c:catch>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원탈퇴</title>
</head>
<body>
<table class="w3-table">
<tr><td>아이디</td><td>${user.userid}</td></tr>
<tr><td>이름</td><td>${user.username}</td></tr>
<tr><td>생년월일</td>
<td><fmt:formatDate value="${user.birthday}" pattern="yyyy-MM-dd"/></td></tr>
</table>
<form action="delete" method="post" name="deleteForm">
<input type="hidden" name="userid" value="${param.userid}">
비밀번호 : <input type="password" name="password" class="w3-input">
<a href="javascript:document.deleteForm.submit()">[회원탈퇴]</a>
</form> 

</body>
</html>