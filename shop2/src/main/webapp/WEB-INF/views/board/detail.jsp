<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%-- fn 라이브러리는 이제 필요 없습니다. --%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${board.title} - ${boardName}</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <style>
        .board-content {
            min-height: 150px;
            white-space: pre-line;
        }
    </style>
</head>
<body>
    <div class="container mt-5">
        <h2 class="mb-3">${boardName}</h2>

        <div class="card shadow-sm">
            <div class="card-header bg-light">
                <h4 class="card-title mb-1">${board.title}</h4>
                <div class="d-flex justify-content-between text-muted small">
                    <div>
                        <span class="me-3"><i class="bi bi-person-fill"></i> ${board.writer}</span>
                        <span><i class="bi bi-calendar-check"></i> <fmt:formatDate value="${board.regdate}" pattern="yyyy-MM-dd HH:mm"/></span>
                    </div>
                    <span>조회수: ${board.readcnt}</span>
                </div>
            </div>

            <div class="card-body p-4">
                <h5 class="mt-2"><i class="bi bi-body-text"></i> 상세 내용</h5>
                <hr>
                <div class="board-content mb-4">
                    ${board.content} <%-- 이제 fn:trim()은 필요 없습니다. --%>
                </div>

                <c:if test="${!empty board.fileurl}">
                    <hr>
                    <div class="mt-3">
						<h6 class="card-subtitle text-muted">
							<i class="bi bi-paperclip"></i> 첨부파일
						</h6>
                        <a href="file/${board.fileurl}" class="text-decoration-none ms-3 d-block mt-2">
                            ${board.fileurl}
                        </a>
                    </div>
                </c:if>
            </div>
        </div>

        <div class="d-flex justify-content-between mt-4">
            <div>
                <a href="list?boardid=${board.boardid}" class="btn btn-secondary">
                    <i class="bi bi-list-ul"></i> 목록
                </a>
            </div>
            <div>
                <a href="reply?num=${board.num}" class="btn btn-primary me-1">
                    <i class="bi bi-reply-fill"></i> 답변
                </a>
                <a href="update?num=${board.num}&boardid=${board.boardid}" class="btn btn-outline-primary me-1">
                    <i class="bi bi-pencil-square"></i> 수정
                </a>
                <a href="delete?num=${board.num}&boardid=${board.boardid}" class="btn btn-outline-danger">
                    <i class="bi bi-trash3"></i> 삭제
                </a>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>