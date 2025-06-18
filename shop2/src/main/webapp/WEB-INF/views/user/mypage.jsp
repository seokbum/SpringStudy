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
<title>마이페이지</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
</head>
<body>

<div class="container my-5">
    <h2 class="mb-4">마이페이지</h2>

    <ul class="nav nav-tabs" id="myPageTab" role="tablist">
        <li class="nav-item" role="presentation">
            <button class="nav-link active" id="minfo-tab" data-bs-toggle="tab" data-bs-target="#minfo-pane" type="button" role="tab" aria-controls="minfo-pane" aria-selected="true">회원정보</button>
        </li>
       
        <li class="nav-item" role="presentation">
            <button class="nav-link" id="oinfo-tab" data-bs-toggle="tab" data-bs-target="#oinfo-pane" type="button" role="tab" aria-controls="oinfo-pane" aria-selected="false">주문정보</button>
        </li>
    </ul>

    <div class="tab-content" id="myPageTabContent">
        <div class="tab-pane fade show active p-4 border border-top-0" id="minfo-pane" role="tabpanel" aria-labelledby="minfo-tab">
            <table class="table table-bordered">
                <tbody>
                    <tr><th style="width:20%; background-color:#f8f9fa;">아이디</th><td>${user.userid}</td></tr>
                    <tr><th style="background-color:#f8f9fa;">이름</th><td>${user.username}</td></tr>
                    <tr><th style="background-color:#f8f9fa;">우편번호</th><td>${user.postcode}</td></tr>
                    <tr><th style="background-color:#f8f9fa;">전화번호</th><td>${user.phoneno}</td></tr>
                    <tr><th style="background-color:#f8f9fa;">이메일</th><td>${user.email}</td></tr>
                    <tr><th style="background-color:#f8f9fa;">생년월일</th><td><fmt:formatDate value="${user.birthday}" pattern="yyyy-MM-dd"/></td></tr>
                </tbody>
            </table>
            
            <div class="mt-4 d-flex justify-content-between align-items-center">
                <div>
                    <a href="logout" class="btn btn-outline-secondary btn-sm">로그아웃</a>
                    <a href="update?userid=${user.userid}" class="btn btn-primary btn-sm">회원정보 수정</a>
                    <a href="password" class="btn btn-secondary btn-sm">비밀번호 수정</a>
                    <c:if test="${loginUser.userid != 'admin'}">
                        <a href="delete?userid=${user.userid}" class="btn btn-danger btn-sm">회원탈퇴</a>
                    </c:if>
                    <c:if test="${loginUser.userid == 'admin'}">
                        <a href="../admin/list" class="btn btn-info btn-sm">회원목록</a>
                    </c:if>
                    <%---- 회원목록 완성해보기 --%>
                </div>
                <div>
                    <a href="../item/list" class="btn btn-success btn-sm">
                        <i class="bi bi-list-ul"></i> 상품 보기
                    </a>
                </div>
            </div>
        </div>

        <div class="tab-pane fade p-3 border border-top-0" id="oinfo-pane" role="tabpanel" aria-labelledby="oinfo-tab">
            <div class="accordion" id="orderAccordion">
                <c:if test="${empty salelist}">
                    <div class="alert alert-warning text-center" role="alert">
                      주문 내역이 없습니다.
                    </div>
                </c:if>
                <c:forEach items="${salelist}" var="sale" varStatus="stat">
                    <div class="accordion-item">
                        <h2 class="accordion-header" id="heading${stat.index}">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapse${stat.index}" aria-expanded="false" aria-controls="collapse${stat.index}">
                                <div class="w-100 d-flex justify-content-between">
                                    <span><strong>주문번호:</strong> ${sale.saleid}</span>
                                    <span><strong>주문일자:</strong> <fmt:formatDate value="${sale.saledate}" pattern="yyyy-MM-dd"/></span>
                                    <span class="me-5"><strong>주문금액:</strong> <fmt:formatNumber value="${sale.total}" pattern="###,###"/>원</span>
                                </div>
                            </button>
                        </h2>
                        <div id="collapse${stat.index}" class="accordion-collapse collapse" aria-labelledby="heading${stat.index}" data-bs-parent="#orderAccordion">
                            <div class="accordion-body">
                                <table class="table table-striped table-sm">
                                    <thead>
                                        <tr><th>상품명</th><th>상품가격</th><th>주문수량</th><th>상품총액</th></tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${sale.itemList}" var="saleItem">
                                            <tr>
                                                <td>${saleItem.item.name}</td>
                                                <td><fmt:formatNumber value="${saleItem.item.price}" pattern="###,###"/>원</td>
                                                <td>${saleItem.quantity}</td>
                                                <td><fmt:formatNumber value="${saleItem.item.price * saleItem.quantity}" pattern="###,###"/>원</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>