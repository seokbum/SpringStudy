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

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.21/dist/summernote-bs5.min.css" rel="stylesheet">

    <style>
        /* Summernote 드롭다운 및 스타일 충돌 해결 */
        .note-editable {
            font-family: inherit !important;
            font-size: inherit !important;
        }
        .note-dropdown-menu {
            z-index: 1050 !important; /* Bootstrap 5 드롭다운 메뉴와 충돌 방지 */
            min-width: 150px !important;
        }
        .note-fontsize .note-dropdown-menu a {
            font-size: inherit !important; /* 글자 크기 드롭다운 항목 스타일 유지 */
        }
        .note-fontname .note-dropdown-menu a {
            font-family: inherit !important; /* 폰트 드롭다운 항목 스타일 유지 */
        }
    </style>
</head>

<body>

    <div class="container mt-5 mb-5">
        <h2 class="mb-4">게시글 작성</h2>

        <form:form modelAttribute="board" action="write?boardid=${param.boardid}" enctype="multipart/form-data" name="f">
            <%--
            바로 이 부분에 있던 <input type="hidden" name="boardid" ...> 태그가 삭제되었습니다.
            boardid는 form 태그의 action 속성을 통해 한 번만 전송됩니다.
            --%>

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

    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.21/dist/summernote-bs5.min.js"></script>

    <script type="text/javascript">
        $(function() {
            $('#summernote').summernote({
                height: 300,
                lang: "ko-KR",
                placeholder: '여기에 내용을 입력하세요...',
                dialogsInBody: true, // Bootstrap 5 모달 호환성
                fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New', 'Helvetica', 'Merriweather'],
                fontNamesIgnoreCheck: ['Merriweather'], // 웹 폰트 로딩 문제 방지
                fontSizes: ['8', '10', '12', '14', '16', '18', '24', '36'], // 글자 크기 옵션
                toolbar: [
                    ['style', ['style']],
                    ['font', ['bold', 'italic', 'underline', 'clear']],
                    ['fontname', ['fontname']],
                    ['fontsize', ['fontsize']],
                    ['color', ['color']],
                    ['para', ['ul', 'ol', 'paragraph']],
                    ['table', ['table']],
                    ['insert', ['link', 'picture', 'video']],
                    ['view', ['fullscreen', 'codeview', 'help']]
                ],
                callbacks: {
                    onImageUpload: function(images){
                        for(let i=0; i < images.length; i++) {
                            sendFile(images[i]);
                        }
                    }
                }
            });
        });

        function sendFile(file) {
            // 파일 업로드를 위한 데이터를 컨테이너 생성
            let data = new FormData();
            data.append("image", file); // 컨테이너에 이미지 객체 추가

            $.ajax({ // ajax을 이용하여 파일 업로드
                url: "/ajax/uploadImage", // 서버 요청 url
                type: "post", // post 방식으로 요청
                data: data, // 전송데이터
                processData: false, // 문자열 전송 아님. 파일 업로드시 사용
                contentType: false, // 컨텐트 타입 자동 설정 안함, 파일 업로드시 사용
                success: function(src) { // 서버응답 완료, 정상처리
                    $("#summernote").summernote("insertImage", src);
                },
                error: function(e) { // 서버 응답오류
                    alert("이미지 업로드에 실패했습니다: " + e.status);
                }
            });
        }
    </script>

</body>
</html>