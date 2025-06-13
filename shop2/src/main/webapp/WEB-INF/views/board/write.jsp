<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>게시글 작성</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body>
    <div class="container mt-5 mb-5">
        <h2 class="mb-4">게시글 작성</h2>

        <form:form modelAttribute="board" action="write" enctype="multipart/form-data" name="f">
        <input type="hidden" name="boardid" value="${param.boardid}">
            
            <div class="mb-3">
                <form:label path="writer" class="form-label">글쓴이</form:label>
                <form:input path="writer" class="form-control" placeholder="작성자 이름을 입력하세요"/>
                <div class="text-danger small mt-1">
                    <form:errors path="writer"/>
                </div>
            </div>

            <div class="mb-3">
                <form:label path="pass" class="form-label">비밀번호</form:label>
                <form:password path="pass" class="form-control" placeholder="비밀번호를 입력하세요"/>
                <div class="text-danger small mt-1">
                    <form:errors path="pass"/>
                </div>
            </div>

            <div class="mb-3">
                <form:label path="title" class="form-label">제목</form:label>
                <form:input path="title" class="form-control" placeholder="제목을 입력하세요"/>
                <div class="text-danger small mt-1">
                    <form:errors path="title"/>
                </div>
            </div>

            <div class="mb-3">
                <form:label path="content" class="form-label">내용</form:label>
                <form:textarea path="content" rows="10" class="form-control" placeholder="내용을 입력하세요"/>
                <div class="text-danger small mt-1">
                    <form:errors path="content"/>
                </div>
            </div>

            <div class="mb-4">
                <label for="file1" class="form-label">첨부파일</label>
                <input type="file" name="file1" id="file1" class="form-control">
            </div>

            <hr>

            <div class="d-flex justify-content-between">
                <a href="list?boardid=${param.boardid}" class="btn btn-secondary">
                    <i class="bi bi-list-ul"></i> 게시글 목록
                </a>
                <a href="javascript:document.f.submit()" class="btn btn-primary">
                    <i class="bi bi-pencil-fill"></i> 게시글 등록
                </a>
            </div>
            
        </form:form>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>