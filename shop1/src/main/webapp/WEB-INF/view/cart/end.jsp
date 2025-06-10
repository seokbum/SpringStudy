<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>주문 완료</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
</head>
<body>

<div class="container my-5">
    <div class="alert alert-success text-center py-4" role="alert">
        <h4 class="alert-heading"><i class="bi bi-check-circle-fill"></i> 주문이 성공적으로 완료되었습니다!</h4>
        <p class="mb-0">${sessionScope.loginUser.username}님의 주문에 감사드립니다. (주문번호: <strong>${sale.saleid}</strong>)</p>
    </div>

    <div class="row g-4">
        <div class="col-lg-12">
            <div class="card">
                <div class="card-header"><h5><i class="bi bi-truck"></i> 배송지 정보</h5></div>
                <div class="card-body">
                    <table class="table table-bordered mb-0">
                        <tbody>
                            <tr>
                                <th class="w-25 bg-light">받는 분</th>
                                <td>${sessionScope.loginUser.username}</td>
                            </tr>
                            <tr>
                                <th class="bg-light">연락처</th>
                                <td>${sessionScope.loginUser.phoneno}</td>
                            </tr>
                            <tr>
                                <th class="bg-light">우편번호</th>
                                <td>${sessionScope.loginUser.postcode}</td>
                            </tr>
                            <tr>
                                <th class="bg-light">배송 주소</th>
                                <td>${sessionScope.loginUser.address}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="col-lg-12">
            <div class="card">
                <div class="card-header"><h5><i class="bi bi-cart-check"></i> 주문 상품 목록</h5></div>
                <div class="card-body">
                    <table class="table table-hover">
                        <thead class="text-center">
                            <tr><th>상품명</th><th>가격</th><th>수량</th><th>합계</th></tr>
                        </thead>
                        <tbody class="text-center">
                            <c:forEach items="${sale.itemList}" var="saleitem">
                                <tr class="align-middle">
                                    <td class="text-start">${saleitem.item.name}</td>
                                    <td class="text-end pe-4"><fmt:formatNumber value="${saleitem.item.price}" pattern="###,###" />원</td>
                                    <td><fmt:formatNumber value="${saleitem.quantity}" pattern="###,###" /></td>
                                    <td class="text-end pe-4"><fmt:formatNumber value="${saleitem.item.price * saleitem.quantity}" pattern="###,###"/>원</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                        <tfoot>
                            <tr>
                                <td colspan="4" class="text-end fs-5 fw-bold pt-3 border-0">
                                    총 결제 금액: <span class="text-primary"><fmt:formatNumber value="${sale.total}" pattern="###,###" />원</span>
                                </td>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>
    </div>
    
    <div class="text-center mt-5">
        <a href="../item/list" class="btn btn-primary btn-lg">
            <i class="bi bi-house-door"></i> 계속 쇼핑하기
        </a>
        <a href="../user/mypage?userid=${loginUser.userid}" class="btn btn-outline-secondary btn-lg">
            <i class="bi bi-receipt"></i> 주문 내역 확인 (마이페이지)
        </a>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>