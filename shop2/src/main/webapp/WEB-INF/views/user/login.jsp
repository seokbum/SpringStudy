<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>로그인화면</title>
    <!-- Bootstrap CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <style>
        body {
            background: linear-gradient(135deg, #e9ecef 0%, #f8f9fa 100%);
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .login-container {
            background-color: white;
            padding: 2.5rem;
            border-radius: 16px;
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 420px;
            border: 1px solid #e0e0e0;
        }
        h2 {
            color: #2c3e50;
            text-align: center;
            margin-bottom: 2rem;
            font-size: 1.9rem;
            font-weight: 700;
            letter-spacing: 0.5px;
        }
        .form-label {
            font-weight: 600;
            color: #34495e;
            font-size: 0.95rem;
            margin-bottom: 0.5rem;
        }
        .form-control {
            border-radius: 10px;
            padding: 0.8rem 1.2rem;
            font-size: 0.95rem;
            border: 1px solid #d1d5db;
            background-color: #f9fbfc;
            transition: all 0.3s ease;
        }
        .form-control:focus {
            border-color: #007bff;
            box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.2);
            background-color: #ffffff;
        }
        .error {
            color: #e74c3c;
            font-size: 0.8rem;
            margin-top: 0.4rem;
            min-height: 1.2rem;
            font-weight: 500;
        }
        .btn {
            border-radius: 10px;
            padding: 0.7rem 1.5rem;
            font-size: 0.95rem;
            font-weight: 600;
            transition: all 0.3s ease;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        .btn-primary {
            background-color: #007bff;
            border: none;
        }
        .btn-primary:hover {
            background-color: #0062cc;
            transform: translateY(-2px);
        }
        .btn-secondary {
            background-color: #6c757d;
            border: none;
        }
        .btn-secondary:hover {
            background-color: #545b62;
            transform: translateY(-2px);
        }
        .button-group {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            gap: 0.75rem;
            margin-top: 2rem;
        }
        @media (max-width: 576px) {
            .login-container {
                padding: 1.75rem;
                max-width: 92%;
            }
            .btn {
                width: 100%;
                padding: 0.8rem;
            }
            h2 {
                font-size: 1.6rem;
            }
        }
    </style>
    <script type="text/javascript">
        function win_open(page) {
            var op = "width=500, height=500, left=50, top=150";
            open(page, "", op);
        }
    </script>
</head>
<body>
    <div class="login-container">
        <h2>사용자로그인</h2>
        <form:form modelAttribute="user" method="post" action="login" name="loginForm">
            <spring:hasBindErrors name="user">
                <div class="error">
                    <c:forEach items="${errors.globalErrors}" var="error">
                        <spring:message code="${error.code}"/>
                    </c:forEach>
                </div>
            </spring:hasBindErrors>
            <div class="mb-4">
                <label for="userid" class="form-label">아이디</label>
                <form:input path="userid" class="form-control" id="userid"/>
                <div class="error">
                    <form:errors path="userid"/>
                </div>
            </div>
            <div class="mb-4">
                <label for="password" class="form-label">비밀번호</label>
                <form:password path="password" class="form-control" id="password"/>
                <div class="error">
                    <form:errors path="password"/>
                </div>
            </div>
            <div class="button-group">
                <input type="submit" value="로그인" class="btn btn-primary">
                <input type="button" value="회원가입" onclick="location.href='join'" class="btn btn-secondary">
                <input type="button" value="비밀번호찾기" onclick="win_open('pwSearch')" class="btn btn-secondary">
                <input type="button" value="아이디찾기" onclick="win_open('idSearch')" class="btn btn-secondary">
            </div>
            <a href="${apiURL}">
            <img height="30" src="https://static.nid.naver.com/oauth/small_g_in.PNG"></a>
        </form:form>
    </div>
    <!-- Bootstrap JS and Popper.js CDN -->
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js" integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js" integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy" crossorigin="anonymous"></script>
</body>
</html>