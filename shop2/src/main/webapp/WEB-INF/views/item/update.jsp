<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>상품 수정</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-7">
            <div class="card shadow-sm">
                <div class="card-body p-4 p-md-5">
                    <h2 class="card-title text-center mb-4">상품 수정</h2>

                    <form:form modelAttribute="item" action="update" enctype="multipart/form-data">
                        <form:hidden path="id"/>
                        <form:hidden path="pictureUrl"/>

                        <div class="mb-3">
                            <label for="name" class="form-label">상품명</label>
                            <form:input path="name" cssClass="form-control" />
                            <div class="text-danger small mt-1"><form:errors path="name"/></div>
                        </div>

                        <div class="mb-3">
                            <label for="price" class="form-label">상품가격</label>
                            <form:input path="price" cssClass="form-control" />
                            <div class="text-danger small mt-1"><form:errors path="price"/></div>
                        </div>

                        <div class="mb-3">
                            <label for="picture" class="form-label">새 상품이미지</label>
                            <div class="form-text mb-2">
                                <c:if test="${not empty item.pictureUrl}">
                                    현재 파일: ${item.pictureUrl}
                                </c:if>
                                <c:if test="${empty item.pictureUrl}">
                                    현재 등록된 이미지가 없습니다.
                                </c:if>
                            </div>
                            <input type="file" name="picture" class="form-control" id="picture">
                            <div class="form-text">새로운 이미지를 선택하면 기존 이미지가 교체됩니다.</div>
                        </div>

                        <div class="mb-3">
                            <label for="description" class="form-label">상품설명</label>
                            <form:textarea path="description" cssClass="form-control" rows="5"/>
                            <div class="text-danger small mt-1"><form:errors path="description"/></div>
                        </div>

                        <div class="d-grid gap-2 d-md-flex justify-content-md-end mt-4">
                            <a href="list" class="btn btn-outline-secondary">상품 목록</a>
                            <input type="submit" value="수정하기" class="btn btn-primary">
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>