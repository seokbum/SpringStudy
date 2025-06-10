<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>회원가입</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<style>
    body { background-color: #f8f9fa; }
    .form-container { max-width: 700px; margin: 5rem auto; }
</style>
</head>
<body>

<div class="form-container">
    <div class="card shadow-lg border-0">
        <div class="card-body p-4 p-md-5">
            <h2 class="card-title text-center mb-4">회원가입</h2>
            <p class="text-center text-muted mb-4">새로운 계정을 만들어 서비스를 이용해 보세요.</p>
            
            <form:form modelAttribute="user" method="post" action="join">
                <spring:hasBindErrors name="user">
                    <c:if test="${errors.hasGlobalErrors()}">
                        <div class="alert alert-danger">
                            <c:forEach items="${errors.globalErrors}" var="error">
                                <spring:message code="${error.code}" /><br>
                            </c:forEach>
                        </div>
                    </c:if>
                </spring:hasBindErrors>

                <div class="row g-3">
                    <div class="col-md-6 form-floating">
                        <form:input path="userid" cssClass="form-control" id="userid" placeholder="아이디" />
                        <label for="userid" class="ms-2">아이디</label>
                        <div class="mt-1 text-danger small ms-2"><form:errors path="userid"/></div>
                    </div>
                    <div class="col-md-6 form-floating">
                        <form:password path="password" cssClass="form-control" id="password" placeholder="비밀번호" />
                        <label for="password" class="ms-2">비밀번호</label>
                        <div class="mt-1 text-danger small ms-2"><form:errors path="password" /></div>
                    </div>
                    <div class="col-md-6 form-floating">
                        <form:input path="username" cssClass="form-control" id="username" placeholder="이름" />
                        <label for="username" class="ms-2">이름</label>
                        <div class="mt-1 text-danger small ms-2"><form:errors path="username"/></div>
                    </div>
                    <div class="col-md-6 form-floating">
                        <form:input path="phoneno" cssClass="form-control" id="phoneno" placeholder="전화번호" />
                        <label for="phoneno" class="ms-2">전화번호</label>
                    </div>
                    <div class="col-md-6 form-floating">
                        <form:input path="postcode" cssClass="form-control" id="postcode" placeholder="우편번호" />
                        <label for="postcode" class="ms-2">우편번호</label>
                    </div>
                    <div class="col-md-6 form-floating">
                        <form:input path="address" cssClass="form-control" id="address" placeholder="주소" />
                        <label for="address" class="ms-2">주소</label>
                    </div>
                     <div class="col-md-6 form-floating">
                        <form:input path="email" cssClass="form-control" id="email" placeholder="이메일" />
                        <label for="email" class="ms-2">이메일</label>
                        <div class="mt-1 text-danger small ms-2"><form:errors path="email" /></div>
                    </div>
                    <div class="col-md-6 form-floating">
                        <form:input path="birthday" cssClass="form-control" id="birthday" placeholder="생년월일 (YYYY-MM-DD)" />
                        <label for="birthday" class="ms-2">생년월일 (YYYY-MM-DD)</label>
                        <div class="mt-1 text-danger small ms-2"><form:errors path="birthday" /></div>
                    </div>
                </div>

                <div class="d-grid gap-2 mt-4">
                    <button type="submit" class="btn btn-primary btn-lg">가입하기</button>
                    <button type="reset" class="btn btn-outline-secondary">다시 작성</button>
                </div>
                <div class="text-center mt-3">
                    <a href="login">이미 계정이 있으신가요? 로그인</a>
                </div>
            </form:form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>