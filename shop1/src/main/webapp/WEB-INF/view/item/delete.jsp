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
<title>상품 삭제 전 확인</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
</head>
<body>

<div class="container my-5">
    <h2 class="mb-4 text-center"><i class="bi bi-exclamation-triangle-fill text-danger me-2"></i>상품 삭제 전 확인</h2>

    <div class="card shadow-sm mx-auto" style="max-width: 700px;">
        <div class="row g-0">
            <div class="col-md-5 d-flex align-items-center justify-content-center p-3">
                <img src="../img/${item.pictureUrl}" class="img-fluid rounded-start" alt="상품 이미지">
            </div>
            <div class="col-md-7">
                <div class="card-body">
                    <h5 class="card-title">${item.name}</h5>
                    <ul class="list-group list-group-flush mb-3">
                        <li class="list-group-item">
                            <span class="fw-bold">가격:</span> ${item.price}원
                        </li>
                        <li class="list-group-item">
                            <span class="fw-bold">상품설명:</span> ${item.description}
                        </li>
                    </ul>
                    <hr>
                    <form action="delete" method="post" class="d-grid gap-2">
                        <input type="hidden" name="id" value="${item.id}">
                        <button type="submit" class="btn btn-danger btn-lg">
                            <i class="bi bi-trash-fill me-2"></i>상품 삭제
                        </button>
                        <button type="button" class="btn btn-secondary btn-lg" onclick="location.href='list'">
                            <i class="bi bi-arrow-left-circle-fill me-2"></i>상품 목록으로 돌아가기
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>