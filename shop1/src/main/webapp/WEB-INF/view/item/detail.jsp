<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>🛒 상품 상세보기</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<div class="container my-5">
    <div class="row mb-3">
        <div class="col">
            <h2 class="border-bottom pb-2">상품 상세보기</h2>
        </div>
    </div>

    <div class="card shadow-sm">
        <div class="card-body p-5">
            <div class="row g-5">
                <div class="col-md-6">
                    <img alt="${item.name}" src="../img/${item.pictureUrl}" class="img-fluid rounded shadow-lg">
                </div>
                
                <div class="col-md-6 d-flex flex-column">
                    
                    <h3>${item.name}</h3>
                    
                    <p class="text-muted">${item.description}</p>
                    
                    <h4 class="mt-3 mb-4 text-danger">${item.price}원</h4>
                    
                    <hr>
                    
                    <form action="../cart/cartAdd" class="mt-auto">
                        <input type="hidden" name="id" value="${item.id}">
                        
                        <div class="mb-3">
                            <label for="quantitySelect" class="form-label"><strong>수량 선택</strong></label>
                            <select name="quantity" id="quantitySelect" class="form-select" style="width: 150px;">
                                <c:forEach begin="1" end="10" var="i">
                                    <option value="${i}">${i}</option>
                                </c:forEach>
                            </select>
                        </div>
                        
                        <div class="d-grid gap-2 d-md-block mt-4">
                            <button type="submit" class="btn btn-primary btn-lg">장바구니 담기</button>
                            <a href="list" class="btn btn-outline-secondary btn-lg">상품 목록</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>