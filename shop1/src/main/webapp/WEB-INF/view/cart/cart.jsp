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
<title>장바구니</title>
<!-- 부트스트랩 5 CSS CDN -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<div class="container mt-5">
    <!-- 페이지 제목 -->
    <h2 class="mb-4">장바구니</h2>
    <!-- 부트스트랩 테이블 적용 -->
    <table class="table table-striped table-hover">
        <thead class="table-dark">
            <tr>
                <th colspan="4" scope="col">장바구니 상품 목록</th>
            </tr>
            <tr>
                <th scope="col">상품명</th>
                <th scope="col">가격</th>
                <th scope="col">수량</th>
                <th scope="col">합계</th>
            </tr>
        </thead>
        <tbody>
            <!-- 장바구니 항목 반복 출력 -->
            <c:forEach items="${cart.itemSetList}" var="set" varStatus="stat">
                <tr>
                    <td>${set.item.name}</td>
                    <td><fmt:formatNumber value="${set.item.price}" pattern="###,###"/>원</td>
                    <td><fmt:formatNumber value="${set.quantity}" pattern="###,###"/></td>
                    <td>
                        <fmt:formatNumber value="${set.quantity * set.item.price}" pattern="###,###"/>원
                        <!-- 삭제 버튼 -->
                        <a href="cartDelete?index=${stat.index}" class="btn btn-danger btn-sm ms-2" aria-label="상품 삭제">ⓧ</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
        <tfoot>
            <!-- 총 금액 표시 -->
            <tr>
                <td colspan="4" class="text-end fw-bold">
                    총 구입 금액: <fmt:formatNumber value="${cart.total}" pattern="###,###"/>원
                </td>
            </tr>
        </tfoot>
    </table>
    <!-- 메시지 출력 (오류나 알림 메시지) -->
    <c:if test="${not empty message}">
        <div class="alert alert-info" role="alert">
            ${message}
        </div>
    </c:if>
    <!-- 버튼 그룹 -->
    <div class="d-flex justify-content-between mt-3">
        <a href="../item/list" class="btn btn-primary">상품목록</a>
        <a href="checkout" class="btn btn-success">주문하기</a>
    </div>
</div>
<!-- 부트스트랩 5 JS와 Popper.js CDN -->
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js" integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js" integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy" crossorigin="anonymous"></script>
</body>
</html>