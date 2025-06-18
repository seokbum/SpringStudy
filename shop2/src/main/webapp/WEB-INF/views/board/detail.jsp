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
    <div class="container mt-5 mb-5">
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
                    ${board.content}
                </div>

                <c:if test="${!empty board.fileurl}">
                    <hr>
                    <div class="mt-3">
                  <h6 class="card-subtitle text-muted">
                     <i class="bi bi-paperclip"></i> 첨부파일
                  </h6>
                        <a href="../file/${board.fileurl}" class="text-decoration-none ms-3 d-block mt-2">
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
                <a href="reply?num=${board.num}&boardid=${board.boardid}" class="btn btn-primary me-1">
                    <i class="bi bi-reply-fill"></i> 답변
                </a>
                <a href="update?num=${board.num}&boardid=${board.boardid}" class="btn btn-outline-primary me-1">
                    <i class="bi bi-pencil-square"></i> 수정
                </a>
                <a href="delete?num=${board.num}&boardid=${board.boardid}" 
                class="btn btn-outline-danger">
                    <i class="bi bi-trash3"></i> 삭제
                </a>
            </div>
        </div>
        <div class="card shadow-sm mt-5">
            <div class="card-body">
            <%--#comment주소로 매핑 시 여기로 옴 --%>
            <span id="comment"></span>
                <h5 class="card-title mb-4"><i class="bi bi-chat-square-dots-fill"></i> 댓글</h5>

                <%-- 댓글 목록 표시 --%>
                
                <div class="comment-list">
                    <c:if test="${empty commlist}">
                        <p class="text-muted">아직 댓글이 없습니다. 첫 댓글을 남겨보세요!</p>
                    </c:if>
                    <c:forEach items="${commlist}" var="c" varStatus="stat">
                        <div class="d-flex mb-3">
                            <div class="flex-shrink-0">
                                <i class="bi bi-person-circle fs-4 text-secondary"></i>
                            </div>
                            <div class="ms-3 w-100">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <span class="fw-bold">${c.writer}</span>
                                        <small class="text-muted ms-2">
                                            <fmt:formatDate value="${c.regdate}" pattern="yyyy-MM-dd HH:mm:ss E요일"/>
                                        </small>
                                    </div>
                                    <%-- 댓글 삭제 폼 --%>
                                    <form action="commdel" method="post" name="commdel${stat.index}" class="d-flex">
                                        <input type="hidden" name="num" value="${c.num}">
                                        <input type="hidden" name="seq" value="${c.seq}">
                                        <input type="password" name="pass" class="form-control form-control-sm me-1" placeholder="비밀번호" style="width: 120px;">
                                        <button type="submit" class="btn btn-outline-danger btn-sm flex-shrink-0">삭제</button>
                                    </form>
                                </div>
                                <p class="mb-1 mt-1">${c.content}</p>
                            </div>
                        </div>
                        <hr class="my-3">
                    </c:forEach>
                </div>

                <%-- 댓글 작성 폼 --%>
                <div class="comment-form mt-4">
                    <h6 class="mb-3">댓글 작성</h6>
                    <form:form modelAttribute="comment" action="comment" method="post" name="commForm">
                        <form:hidden path="num" value="${board.num}"/>
                        
                        <div class="row g-2 mb-2">
                            <div class="col-md">
                                <form:input path="writer" cssClass="form-control form-control-sm" placeholder="작성자"/>
                                <div class="text-danger small mt-1"><form:errors path="writer"/></div>
                            </div>
                            <div class="col-md">
                                <form:password path="pass" cssClass="form-control form-control-sm" placeholder="비밀번호"/>
                                <div class="text-danger small mt-1"><form:errors path="pass"/></div>
                            </div>
                        </div>
                        
                        <div class="d-flex">
                            <form:textarea path="content" cssClass="form-control" placeholder="댓글을 남겨주세요." rows="1"/>
                            <button type="submit" class="btn btn-primary ms-2 flex-shrink-0">등록</button>
                        </div>
                        <div class="text-danger small mt-1"><form:errors path="content"/></div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>