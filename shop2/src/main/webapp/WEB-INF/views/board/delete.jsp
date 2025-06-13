<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%--
	삭제 구현하기
	1. Controller
		비밀번호가 일치하면 num 해당하는 게시물 삭제.
		비밀번호 오류시 globalError 방식으로 처리하기
	2. service.boardDelete
	   boradDao.delete 메서드 명으로 처리하기
 --%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>게시글 삭제</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<style>
	body {
		background-color: #f8f9fa;
	}
</style>
</head>
<body>
<div class="container">
	<div class="row justify-content-center">
		<div class="col-md-6 col-lg-5">
			<div class="card mt-5 shadow-sm">
				<div class="card-body p-4">
					<h3 class="card-title text-center mb-2">게시글 삭제</h3>
					<p class="text-center text-muted mb-4">
						게시글을 삭제하시려면 비밀번호를 입력하세요.
					</p>

					<form action="delete" method="post" name="f">
						<input type="hidden" name="num" value="${param.num}">
						<input type="hidden" name="boardid" value="${param.boardid}">
						
						<spring:hasBindErrors name="board">
							<div class="alert alert-danger">
								<c:forEach items="${errors.globalErrors}" var="error">
									<spring:message code="${error.code}" />
								</c:forEach>
							</div>
						</spring:hasBindErrors>

						<div class="mb-3">
							<label for="pass" class="form-label">비밀번호</label>
							<input type="password" name="pass" id="pass" class="form-control" autofocus>
						</div>
						
						<div class="d-grid gap-2 mt-4">
							<button type="submit" class="btn btn-danger">
								<i class="bi bi-trash3"></i> 삭제
							</button>
							<a href="detail?num=${param.num}" class="btn btn-secondary">
								취소
							</a>
						</div>
					</form>
				</div>
			</div>
			
			<div class="text-center mt-3">
				<a href="list?boardid=${param.boardid}" class="text-decoration-none">
					<i class="bi bi-list-ul"></i> 게시물 목록으로 돌아가기
				</a>
			</div>
		</div>
	</div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>