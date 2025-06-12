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
<title>비밀번호 찾기</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
</head>
<body>

<div class="container my-5">
    <h2 class="mb-4 text-center"><i class="bi bi-key-fill me-2"></i>비밀번호 찾기</h2>

    <div class="row justify-content-center">
        <div class="col-md-6 col-lg-5">
            <div class="card shadow-sm p-4">
                <form:form modelAttribute="user" action="pwsearch" method="post">
                    <spring:hasBindErrors name="user">
                        <div class="alert alert-danger mb-3" role="alert">
                            <c:forEach items="${errors.globalErrors}" var="error">
                                <spring:message code="${error.code}"/><br>
                            </c:forEach>
                        </div>
                    </spring:hasBindErrors>

                    <div class="mb-3">
                        <label for="userid" class="form-label">아이디</label>
                        <input type="text" class="form-control" id="userid" name="userid" placeholder="아이디를 입력하세요">
                        <form:errors path="userid" cssClass="text-danger"/>
                    </div>

                    <div class="mb-3">
                        <label for="email" class="form-label">이메일</label>
                        <input type="email" class="form-control" id="email" name="email" placeholder="이메일 주소를 입력하세요">
                        <form:errors path="email" cssClass="text-danger"/>
                    </div>

                    <div class="mb-3">
                        <label for="phoneno" class="form-label">전화번호</label>
                        <input type="text" class="form-control" id="phoneno" name="phoneno" placeholder="전화번호를 입력하세요 (예: 010-1234-5678)">
                        <form:errors path="phoneno" cssClass="text-danger"/>
                    </div>

                    <div class="d-grid mt-4">
                        <button type="submit" class="btn btn-primary btn-lg">
                            <i class="bi bi-search me-2"></i>비밀번호 찾기
                        </button>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>