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
<title>로그인</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<style>
    /* 페이지 전체를 회색 배경으로 설정하고, 폼을 수직 중앙 정렬하기 위한 스타일 */
    body, html {
        height: 100%;
        background-color: #f8f9fa;
    }
    .form-container {
        display: flex;
        align-items: center;
        justify-content: center;
        min-height: 100%;
    }
</style>
<script type="text/javascript">
    // 팝업창 기능은 그대로 유지합니다.
    function win_open(page) {
        var op = "width=500, height=350, left=50, top=150";
        open(page, "", op);
    }
</script>
</head>
<body>

<div class="form-container">
    <div class="col-md-5 col-lg-4">
        <div class="card shadow-lg border-0">
            <div class="card-body p-4 p-md-5">
                <h2 class="card-title text-center mb-4">로그인</h2>

                <form:form modelAttribute="user" method="post" action="login" name="loginform">
                    
                    <spring:hasBindErrors name="user">
                        <c:if test="${errors.hasGlobalErrors()}">
                            <div class="alert alert-danger mb-3">
                                <c:forEach items="${errors.globalErrors}" var="error">
                                    <spring:message code="${error.code}" />
                                </c:forEach>
                            </div>
                        </c:if>
                    </spring:hasBindErrors>
                    
                    <div class="form-floating mb-3">
                        <form:input path="userid" cssClass="form-control" id="userid" placeholder="아이디" />
                        <label for="userid">아이디</label>
                        <div class="mt-1 text-danger small">
                            <form:errors path="userid"/>
                        </div>
                    </div>

                    <div class="form-floating mb-4">
                        <form:password path="password" cssClass="form-control" id="password" placeholder="비밀번호" />
                        <label for="password">비밀번호</label>
                        <div class="mt-1 text-danger small">
                            <form:errors path="password"/>
                        </div>
                    </div>

                    <div class="d-grid mb-3">
                        <input type="submit" value="로그인" class="btn btn-primary btn-lg">
                    </div>

                    <div class="text-center text-muted small">
                        <a href="join" class="text-decoration-none">회원가입</a>
                        <span class="mx-1">|</span>
                        <a href="javascript:win_open('idsearch')" class="text-decoration-none">아이디 찾기</a>
                        <span class="mx-1">|</span>
                        <a href="javascript:win_open('pwsearch')" class="text-decoration-none">비밀번호 찾기</a>
                    </div>

                </form:form>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>