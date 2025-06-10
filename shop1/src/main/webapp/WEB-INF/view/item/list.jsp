<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>🛍️ 상품 목록</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
</head>
<body>

<div class="container mt-5">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2 class="mb-0">상품 목록</h2>
        
        <div>
            <a href="create" class="btn btn-primary">
                <i class="bi bi-plus-circle"></i> 상품 등록
            </a>
            
            <c:if test="${not empty sessionScope.loginUser}">
                <a href="../user/mypage?userid=${sessionScope.loginUser.userid}" class="btn btn-info">
                    <i class="bi bi-person-circle"></i> 마이페이지
                </a>
            </c:if>

            <a href="../cart/cartView" class="btn btn-outline-secondary">
                <i class="bi bi-cart"></i> 장바구니 보기
            </a>
        </div>
    </div>

    <table class="table table-hover table-striped">
        <thead class="table-dark">
            <tr>
                <th scope="col" class="text-center" style="width: 10%;">상품ID</th>
                <th scope="col" class="text-center" style="width: 50%;">상품명</th>
                <%-- 가격 헤더: text-end -> text-center 로 수정 --%>
                <th scope="col" class="text-center" style="width: 15%;">가격</th>
                <th scope="col" class="text-center" style="width: 12.5%;">수정</th>
                <th scope="col" class="text-center" style="width: 12.5%;">삭제</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${itemList}" var="item">
                <tr class="align-middle">
                    <td class="text-center">${item.id}</td>
                    <td class="text-center">
                        <a href="detail?id=${item.id}" class="text-decoration-none">${item.name}</a>
                    </td>
                    <%-- 가격 내용: text-end -> text-center 로 수정 --%>
                    <td class="text-center"><fmt:formatNumber value="${item.price}" pattern="###,###"/>원</td>
                    <td class="text-center">
                        <a href="update?id=${item.id}" class="btn btn-sm btn-warning">수정</a>
                    </td>
                    <td class="text-center">
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