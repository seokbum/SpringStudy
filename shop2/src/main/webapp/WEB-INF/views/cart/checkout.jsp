<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%-- <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %> --%>
<%-- <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>주문 전 상품 목록 확인</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container my-5">
    <h2 class="mb-4 border-bottom pb-2">주문 정보 확인</h2>

    <div class="card mb-4">
        <div class="card-header">
            <h5>🚚 배송지 정보</h5>
        </div>
        <div class="card-body">
            <table class="table table-bordered">
                <tbody>
                    <tr>
                        <th style="width: 20%; background-color: #f8f9fa;">주문자 ID</th>
                        <td>${sessionScope.loginUser.userid}</td>
                    </tr>
                    <tr>
                        <th style="background-color: #f8f9fa;">이름</th>
                        <td>${sessionScope.loginUser.username}</td>
                    </tr>
                    <tr>
                        <th style="background-color: #f8f9fa;">우편번호</th>
                        <td>${sessionScope.loginUser.postcode}</td>
                    </tr>
                    <tr>
                        <th style="background-color: #f8f9fa;">주소</th>
                        <td>${sessionScope.loginUser.address}</td>
                    </tr>
                    <tr>
                        <th style="background-color: #f8f9fa;">연락처</th>
                        <td>${sessionScope.loginUser.phoneno}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

    <div class="card">
        <div class="card-header">
            <h5>🛍️ 구매 상품 목록</h5>
        </div>
        <div class="card-body">
            <table class="table table-hover">
                <thead class="text-center">
                    <tr>
                        <th>상품명</th>
                        <th>가격</th>
                        <th>수량</th>
                        <th>합계</th>
                    </tr>
                </thead>
                <tbody class="text-center">
                    <c:forEach items="${sessionScope.CART.itemSetList}" var="itemSet">
                        <tr class="align-middle">
                            <td class="text-start">${itemSet.item.name}</td>
                            <td class="text-end pe-4"><fmt:formatNumber value="${itemSet.item.price}" pattern="###,###" />원</td>
                            <td><fmt:formatNumber value="${itemSet.quantity}" pattern="###,###" /></td>
                            <td class="text-end pe-4"><fmt:formatNumber value="${itemSet.item.price * itemSet.quantity}" pattern="###,###" />원</td>
                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="4" class="text-end fs-5 fw-bold pt-3">
                            총 결제 금액: <span class="text-primary"><fmt:formatNumber value="${sessionScope.CART.total}" pattern="###,###" />원</span>
                        </td>
                    </tr>
                </tfoot>
            </table>
        </div>
    </div>

    <div class="d-flex justify-content-between mt-4">
        <a href="../item/list" class="btn btn-outline-secondary btn-lg">‹ 이전 (상품 목록)</a>
        <a href="end" class="btn btn-success btn-lg">주문 확정하기 ›</a>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>