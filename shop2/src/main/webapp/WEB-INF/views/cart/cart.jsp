<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>장바구니</title>
<!-- Bootstrap CSS CDN -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<style>
    body {
        background-color: #f8f9fa;
        padding: 20px;
    }
    .cart-container {
        max-width: 800px;
        margin: 0 auto;
        background: white;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    }
    .table {
        margin-bottom: 20px;
    }
    .table th, .table td {
        vertical-align: middle;
        text-align: center;
    }
    .delete-btn {
        color: #dc3545;
        text-decoration: none;
        font-size: 1.2em;
        transition: color 0.2s;
    }
    .delete-btn:hover {
        color: #b02a37;
    }
    .total-row {
        font-weight: bold;
        background-color: #f1f1f1;
    }
    .action-links {
        display: flex;
        gap: 15px;
        justify-content: center;
        margin-top: 20px;
    }
    .action-links a {
        padding: 10px 20px;
        border-radius: 5px;
        text-decoration: none;
        font-weight: 500;
        transition: all 0.3s;
    }
    .btn-shop {
        background-color: #6c757d;
        color: white;
    }
    .btn-shop:hover {
        background-color: #5a6268;
    }
    .btn-checkout {
        background-color: #28a745;
        color: white;
    }
    .btn-checkout:hover {
        background-color: #218838;
    }
    .message {
        text-align: center;
        color: #6c757d;
        margin: 15px 0;
    }
</style>
</head>
<body>
<div class="cart-container">
    <h2 class="text-center mb-4">장바구니</h2>
    <table class="table table-striped table-hover">
        <thead class="table-dark">
            <tr>
                <th colspan="4">장바구니 상품목록</th>
            </tr>
            <tr>
                <th>상품명</th>
                <th>가격</th>
                <th>수량</th>
                <th>합계</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${cart.itemSetList}" var="set" varStatus="stat">
                <tr>
                    <td>${set.item.name}</td>
                    <td><fmt:formatNumber value="${set.item.price}" pattern="###,###"/></td>
                    <td><fmt:formatNumber value="${set.quantity}" pattern="###,###"/></td>
                    <td>
                        <fmt:formatNumber value="${set.quantity * set.item.price}" pattern="###,###"/>
                        <a href="cartDelete?index=${stat.index}" class="delete-btn">ⓧ</a>
                    </td>
                </tr>
            </c:forEach>
            <tr class="total-row">
                <td colspan="4" class="text-end">
                    총 구입 금액: <fmt:formatNumber value="${cart.total}" pattern="###,###"/>원
                </td>
            </tr>
        </tbody>
    </table>
    
    <div class="message">${message}</div>
    
    <div class="action-links">
        <a href="../item/list" class="btn-shop">상품목록</a>
        <a href="checkout" class="btn-checkout">주문하기</a>
    </div>
</div>
<!-- Bootstrap JS and Popper.js CDN for interactive components -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>