<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>🛍️ 상품 목록</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<div class="container mt-5">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2 class="mb-0">상품 목록</h2>
        <div>
            <a href="create" class="btn btn-primary">상품 등록</a>
            <a href="../cart/cartView" class="btn btn-outline-secondary">장바구니 보기</a>
        </div>
    </div>

    <table class="table table-hover table-striped text-center">
        <thead class="table-dark">
            <tr>
                <th scope="col" style="width: 10%;">상품ID</th>
                <th scope="col" style="width: 50%;">상품명</th>
                <th scope="col" style="width: 15%;">가격</th>
                <th scope="col" style="width: 12.5%;">수정</th>
                <th scope="col" style="width: 12.5%;">삭제</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${itemList}" var="item">
                <tr class="align-middle">
                    <td>${item.id}</td>
                    <td class="text-start">
                        <a href="detail?id=${item.id}" class="text-decoration-none">${item.name}</a>
                    </td>
                    <td class="text-end pe-3">${item.price}원</td>
                    <td>
                        <a href="update?id=${item.id}" class="btn btn-sm btn-warning">수정</a>
                    </td>
                    <td>
                        <a href="delete?id=${item.id}" class="btn btn-sm btn-danger">삭제</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>