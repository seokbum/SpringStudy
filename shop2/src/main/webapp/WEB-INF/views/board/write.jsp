<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>게시글 작성</title>

    <%-- Bootstrap CSS는 레이아웃에 있으니, 여기서는 Bootstrap Icons만 추가합니다. --%>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    
    <style>
        /* Summernote 드롭다운 및 스타일 충돌 해결. 이 스타일은 최종 HTML <head>에 삽입됩니다. */
        .note-editable {
            font-family: inherit !important;
            font-size: inherit !important;
        }
        .note-dropdown-menu {
            z-index: 1050 !important;
            min-width: 150px !important;
        }
        .note-fontsize .note-dropdown-menu a {
            font-size: inherit !important;
        }
        .note-fontname .note-dropdown-menu a {
            font-family: inherit !important;
        }
    </style>
</head>

<body>

    <div class="container mt-5 mb-5">
        <h2 class="mb-4">게시글 작성</h2>

        <form:form modelAttribute="board" action="write?boardid=${param.boardid}" enctype="multipart/form-data" name="f">
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
                <form:textarea path="content" rows="10" class="form-control" id="summernote"/>
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
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-pencil-fill"></i> 게시글 등록
                </button>
            </div>
        </form:form>
    </div>

    <%-- Summernote 작동을 위해 필요한 JavaScript 라이브러리를 이곳에 다시 포함합니다. --%>
    <%-- 레이아웃에서도 로드되지만, SiteMesh 환경의 특성상 이렇게 해야 오류를 피할 수 있습니다. --%>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-bs5.min.js"></script>

    <script type="text/javascript">
        $(document).ready(function() {
            $('#summernote').summernote({
                height: 300,
                lang: "ko-KR",
                placeholder: '여기에 내용을 입력하세요...',
                dialogsInBody: true,
                fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New', 'Helvetica', 'Merriweather'],
                fontNamesIgnoreCheck: ['Merriweather'],
                fontSizes: ['8', '10', '12', '14', '16', '18', '24', '36'],
                callbacks: {
                    onImageUpload: function(images) {
                        for(let i=0; i < images.length; i++) {
                            sendFile(images[i]);
                        }
                    }
                }
            });
        });

        function sendFile(file) {
            let data = new FormData();
            data.append("image", file);

            $.ajax({
                url: "/ajax/uploadImage",
                type: "post",
                data: data,
                processData: false,
                contentType: false,
                success: function(src) {
                    $("#summernote").summernote("insertImage", src);
                },
                error: function(e) {
                    alert("이미지 업로드에 실패했습니다: " + e.status);
                }
            });
        }
    </script>

</body>
</html>