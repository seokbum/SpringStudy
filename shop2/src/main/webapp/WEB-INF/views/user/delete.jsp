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
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>회원 탈퇴</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
</head>
<body>

<div class="container my-5">
    <h2 class="mb-4 text-center text-danger"><i class="bi bi-person-x-fill me-2"></i>회원 탈퇴</h2>
    <p class="text-center text-muted mb-4">회원 탈퇴 시 모든 정보가 삭제되며, 복구할 수 없습니다. 신중하게 결정해 주세요.</p>

    <div class="row justify-content-center">
        <div class="col-md-7 col-lg-6">
            <div class="card shadow-sm mb-4">
                <div class="card-header bg-danger text-white">
                    <h5 class="card-title mb-0">회원 정보 확인</h5>
                </div>
                <div class="card-body">
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <span class="fw-bold">아이디:</span>
                            <span>${user.userid}</span>
                        </li>
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <span class="fw-bold">이름:</span>
                            <span>${user.username}</span>
                        </li>
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <span class="fw-bold">생년월일:</span>
                            <span><fmt:formatDate value="${user.birthday}" pattern="yyyy-MM-dd"/></span>
                        </li>
                    </ul>
                </div>
            </div>

            <div class="card shadow-sm p-4">
                <h5 class="card-title text-center mb-3">탈퇴를 위해 비밀번호를 입력해주세요</h5>
                <form action="delete" method="post" name="deleteForm">
                    <input type="hidden" name="userid" value="${param.userid}">
                    
                    <div class="mb-3">
                        <label for="password" class="form-label">비밀번호</label>
                        <input type="password" class="form-control" id="password" name="password" placeholder="비밀번호를 입력하세요">
                    </div>
                    
                    <div class="d-grid mt-4">
                        <button type="submit" class="btn btn-danger btn-lg">
                            <i class="bi bi-person-x-fill me-2"></i>회원 탈퇴
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
</script>
</body>
</html>