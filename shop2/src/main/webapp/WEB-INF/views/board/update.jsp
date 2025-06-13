<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>게시글 수정</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<script>
	// 첨부파일 삭제를 위한 Javascript 함수
	function file_delete() {
		// 서버로 전달될 fileurl 값을 비워서 삭제함을 알림
		document.f.fileurl.value = "";
		// 화면에서 파일 표시 영역을 숨김
		document.getElementById("file_desc").style.display="none";
	}
</script>
</head>
<body>
<div class="container mt-5 mb-5">
	<h2 class="mb-4">게시글 수정</h2>

	<form:form modelAttribute="board" action="update" enctype="multipart/form-data" name="f">
		<%-- 수정 대상 게시물 번호와 게시판 ID는 숨겨서 전송 --%>
		<form:hidden path="num"/>
		<form:hidden path="boardid"/>

		<%-- 글쓴이 --%>
		<div class="mb-3">
			<form:label path="writer" class="form-label">글쓴이</form:label>
			<form:input path="writer" class="form-control" />
			<div class="text-danger small mt-1"><form:errors path="writer" /></div>
		</div>

		<%-- 비밀번호 --%>
		<div class="mb-3">
			<form:label path="pass" class="form-label">비밀번호</form:label>
			<form:password path="pass" class="form-control" placeholder="수정/삭제 시 비밀번호를 입력하세요"/>
			<div class="text-danger small mt-1"><form:errors path="pass" /></div>
		</div>

		<%-- 제목 --%>
		<div class="mb-3">
			<form:label path="title" class="form-label">제목</form:label>
			<form:input path="title" class="form-control" />
			<div class="text-danger small mt-1"><form:errors path="title" /></div>
		</div>

		<%-- 내용 --%>
		<div class="mb-3">
			<form:label path="content" class="form-label">내용</form:label>
			<form:textarea path="content" rows="10" class="form-control" />
			<div class="text-danger small mt-1"><form:errors path="content" /></div>
		</div>

		<%-- 첨부파일 --%>
		<div class="mb-4">
			<label class="form-label">첨부파일</label>
			<%-- 기존 첨부파일이 있는 경우에만 표시 --%>
			<c:if test="${!empty board.fileurl}">
				<div class="alert alert-light" id="file_desc">
					<div class="d-flex justify-content-between align-items-center">
						<span>
							<i class="bi bi-paperclip"></i>
							<a href="/file/${board.fileurl}">${board.fileurl}</a>
						</span>
						<button type="button" class="btn btn-sm btn-outline-danger" onclick="file_delete()">삭제</button>
					</div>
				</div>
			</c:if>
			<%-- 파일 삭제 또는 유지를 위해 기존 파일명을 숨겨서 전송 --%>
			<form:hidden path="fileurl"/>
			<input type="file" name="file1" class="form-control">
			<div class="form-text">새 파일을 업로드하면 기존 파일은 대체됩니다.</div>
		</div>

		<hr>

		<%-- 버튼 그룹 --%>
		<div class="d-flex justify-content-end">
			<%-- 목록 링크 오류 수정: boardid 파라미터 추가 --%>
			<a href="list?boardid=${board.boardid}" class="btn btn-secondary me-2">
				<i class="bi bi-list-ul"></i> 게시글 목록
			</a>
			<button type="submit" class="btn btn-primary">
				<i class="bi bi-check-lg"></i> 게시글 수정
			</button>
		</div>
	</form:form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>