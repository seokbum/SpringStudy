<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%-- /WEB-INF/view/admin/list.jsp --%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원 목록</title>
    <!-- Bootstrap CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <!-- jQuery CDN (added for checkbox functionality) -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
    <style>
        body {
            background: linear-gradient(135deg, #e9ecef 0%, #f8f9fa 100%);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            padding: 2rem;
            min-height: 100vh;
        }
        .container {
            max-width: 1000px;
            margin: 0 auto;
            background-color: white;
            padding: 2rem;
            border-radius: 12px;
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
        }
        h2 {
            color: #2c3e50;
            font-size: 1.9rem;
            font-weight: 700;
            margin-bottom: 1.5rem;
            border-bottom: 2px solid #3498db;
            padding-bottom: 0.5rem;
            text-align: center;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 0.9rem;
            text-align: left;
        }
        th {
            background-color: #3498db;
            color: white;
            font-weight: 600;
            text-transform: uppercase;
            font-size: 0.9rem;
        }
        td {
            border-bottom: 1px solid #dee2e6;
            color: #495057;
        }
        .action-links a {
            color: #ffffff;
            text-decoration: none;
            font-weight: 500;
            padding: 0.4rem 0.8rem;
            border-radius: 6px;
            margin-right: 0.5rem;
            transition: all 0.3s ease;
            display: inline-block;
        }
        .action-links a.update {
            background-color: #2ecc71;
        }
        .action-links a.delete {
            background-color: #e74c3c;
        }
        .action-links a.info {
            background-color: #3498db;
        }
        .action-links a:hover {
            opacity: 0.9;
            transform: translateY(-2px);
        }
        .btn-submit {
            background-color: #2ecc71;
            color: white;
            border: none;
            border-radius: 8px;
            padding: 0.7rem 1.5rem;
            font-size: 0.95rem;
            font-weight: 600;
            transition: all 0.3s ease;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        .btn-submit:hover {
            background-color: #27ae60;
            transform: translateY(-2px);
        }
        .checkbox-col {
            text-align: center;
        }
        @media (max-width: 768px) {
            .container {
                padding: 1.5rem;
                max-width: 95%;
            }
            th, td {
                padding: 0.6rem;
                font-size: 0.9rem;
            }
            .action-links a {
                display: block;
                margin-bottom: 0.5rem;
            }
            .btn-submit {
                width: 100%;
                padding: 0.8rem;
            }
        }
    </style>
    <script type="text/javascript">
        function allchkbox(allchk) {
            $(".idchks").prop("checked", allchk.checked);
        }
    </script>
</head>
<body>
    <div class="container">
        <h2>회원목록</h2>
        <form action="mail">
            <table class="table">
                <thead>
                    <tr>
                        <th>아이디</th>
                        <th>이름</th>
                        <th>전화</th>
                        <th>생일</th>
                        <th>이메일</th>
                        <th>작업</th>
                        <th class="checkbox-col">
                            <input type="checkbox" name="allchk" onchange="allchkbox(this)">
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${list}" var="user">
                        <tr>
                            <td>${user.userid}</td>
                            <td>${user.username}</td>
                            <td>${user.phoneno}</td>
                            <td><fmt:formatDate value="${user.birthday}" pattern="yyyy-MM-dd" /></td>
                            <td>${user.email}</td>
                            <td class="action-links">
                                <a href="../user/update?userid=${user.userid}" class="update">수정</a>
                                <a href="../user/delete?userid=${user.userid}" class="delete">강제탈퇴</a>
                                <a href="../user/mypage?userid=${user.userid}" class="info">회원정보</a>
                            </td>
                            <td class="checkbox-col">
                                <input type="checkbox" name="idchks" class="idchks" value="${user.userid}">
                            </td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td colspan="7" class="text-center">
                            <input type="submit" value="메일보내기" class="btn-submit">
                        </td>
                    </tr>
                </tbody>
            </table>
        </form>
    </div>
    <!-- Bootstrap JS and Popper.js CDN -->
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js" integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js" integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy" crossorigin="anonymous"></script>
</body>
</html>