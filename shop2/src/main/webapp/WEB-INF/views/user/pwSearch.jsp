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
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>비밀번호 찾기</title>
    <!-- Bootstrap CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <style>
        body {
            background: linear-gradient(135deg, #ff9a9e 0%, #fad0c4 100%);
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .pw-search-container {
            background-color: white;
            padding: 2.5rem;
            border-radius: 16px;
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
            width: 100%;
            max-width: 450px;
            border: 1px solid #e0e0e0;
        }
        h3 {
            color: #2c3e50;
            text-align: center;
            margin-bottom: 2rem;
            font-size: 1.8rem;
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
            border-color: #e74c3c;
            box-shadow: 0 0 0 0.2rem rgba(231, 76, 60, 0.25);
            background-color: #ffffff;
        }
        .error {
            color: #e74c3c;
            font-size: 0.8rem;
            margin-top: 0.4rem;
            min-height: 1.2rem;
            font-weight: 500;
        }
        .btn-submit {
            background-color: #e74c3c;
            color: white;
            border: none;
            border-radius: 10px;
            padding: 0.7rem 1.5rem;
            font-size: 0.95rem;
            font-weight: 600;
            width: 100%;
            transition: all 0.3s ease;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        .btn-submit:hover {
            background-color: #c0392b;
            transform: translateY(-2px);
        }
        table {
            width: 100%;
        }
        th, td {
            padding: 0.5rem;
        }
        th {
            font-weight: 600;
            color: #34495e;
        }
        @media (max-width: 576px) {
            .pw-search-container {
                padding: 1.75rem;
                max-width: 92%;
            }
            h3 {
                font-size: 1.6rem;
            }
            .btn-submit {
                padding: 0.8rem;
            }
        }
    </style>
</head>
<body>
    <div class="pw-search-container">
        <h3>비밀번호 찾기</h3>
        <form:form modelAttribute="user" action="pwSearch" method="post">
            <spring:hasBindErrors name="user">
                <div class="error">
                    <c:forEach items="${errors.globalErrors}" var="error">
                        <spring:message code="${error.code}"/>
                    </c:forEach>
                </div>
            </spring:hasBindErrors>
            <table>
                <tr>
                    <th><label for="userid" class="form-label">아이디</label></th>
                    <td>
                        <form:input path="userid" class="form-control" id="userid"/>
                        <div class="error">
                            <form:errors path="userid"/>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th><label for="email" class="form-label">이메일</label></th>
                    <td>
                        <form:input path="email" class="form-control" id="email"/>
                        <div class="error">
                            <form:errors path="email"/>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th><label for="phoneno" class="form-label">전화번호</label></th>
                    <td>
                        <form:input path="phoneno" class="form-control" id="phoneno"/>
                        <div class="error">
                            <form:errors path="phoneno"/>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input type="submit" value="비밀번호 찾기" class="btn-submit">
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
    <!-- Bootstrap JS and Popper.js CDN -->
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js" integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js" integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy" crossorigin="anonymous"></script>
</body>
</html>