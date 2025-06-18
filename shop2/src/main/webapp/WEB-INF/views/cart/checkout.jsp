<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>주문 전 상품 목록 보기</title>
    <!-- Bootstrap CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <style>
        body {
            background-color: #f5f7fa;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            padding: 2rem;
            min-height: 100vh;
        }
        .container {
            max-width: 900px;
            margin: 0 auto;
            background-color: white;
            padding: 2rem;
            border-radius: 12px;
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
        }
        h2 {
            color: #2c3e50;
            font-size: 1.75rem;
            font-weight: 600;
            margin-bottom: 1.5rem;
            border-bottom: 2px solid #3498db;
            padding-bottom: 0.5rem;
        }
        .delivery-info, .order-items {
            margin-bottom: 2rem;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 0.75rem;
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
        .delivery-info td:first-child {
            font-weight: 500;
            color: #2c3e50;
            width: 30%;
        }
        .delivery-info td:last-child {
            width: 70%;
        }
        .total-row {
            font-weight: 600;
            background-color: #f8f9fa;
            color: #2c3e50;
        }
        .action-links a {
            color: #ffffff;
            text-decoration: none;
            font-weight: 500;
            padding: 0.6rem 1.2rem;
            border-radius: 8px;
            margin-right: 0.75rem;
            transition: all 0.3s ease;
        }
        .action-links a.confirm {
            background-color: #2ecc71;
        }
        .rediscover {
            background-color: #3498db;
        }
        .action-links a:hover {
            opacity: 0.9;
            transform: translateY(-2px);
        }
        @media (max-width: 576px) {
            .container {
                padding: 1.5rem;
                max-width: 95%;
            }
            th, td {
                padding: 0.5rem;
                font-size: 0.9rem;
            }
            .action-links a {
                display: inline-block;
                width: 100%;
                text-align: center;
                margin-bottom: 0.5rem;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>배송지정보</h2>
        <div class="delivery-info">
            <table class="table">
                <tbody>
                    <tr>
                        <td>주문아이디</td>
                        <td>${sessionScope.loginUser.userid}</td>
                    </tr>
                    <tr>
                        <td>이름</td>
                        <td>${sessionScope.loginUser.username}</td>
                    </tr>
                    <tr>
                        <td>우편번호</td>
                        <td>${sessionScope.loginUser.postcode}</td>
                    </tr>
                    <tr>
                        <td>주소</td>
                        <td>${sessionScope.loginUser.address}</td>
                    </tr>
                    <tr>
                        <td>전화번호</td>
                        <td>${sessionScope.loginUser.phoneno}</td>
                    </tr>
                </tbody>
            </table>
        </div>
        <h2>구매상품</h2>
        <div class="order-items">
            <table class="table">
                <thead>
                    <tr>
                        <th>상품명</th>
                        <th>가격</th>
                        <th>수량</th>
                        <th>합계</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${sessionScope.CART.itemSetList}" var="itemSet" varStatus="stat">
                        <tr>
                            <td>${itemSet.item.name}</td>
                            <td>${itemSet.item.price}원</td>
                            <td>${itemSet.quantity}</td>
                            <td>${itemSet.item.price * itemSet.quantity}원</td>
                        </tr>
                    </c:forEach>
                    <tr class="total-row">
                        <td colspan="4" align="right">
                            총구입금액 : ${sessionScope.CART.total}원
                        </td>
                    </tr>
                </tbody>
            </table>
            <div class="action-links">
                <a href="end" class="confirm">확정하기</a>
                <a href="../item/list" class="rediscover">상품목록</a>
            </div>
        </div>
    </div>
    <!-- Bootstrap JS and Popper.js CDN -->
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js" integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js" integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy" crossorigin="anonymous"></script>
</body>
</html>