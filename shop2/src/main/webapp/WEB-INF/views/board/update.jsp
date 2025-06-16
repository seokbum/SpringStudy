<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>게시글 수정</title>
    
    <%-- Bootstrap 5 CSS 및 아이콘 추가 --%>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    
    <%-- Summernote 에디터 CSS 추가 --%>
    <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-bs5.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5 mb-5">
        <h2 class="mb-4">게시글 수정</h2>

        <form:form modelAttribute="board" action="update" enctype="multipart/form-data" name="f">
            <%-- 업데이트에 필요한 숨김 필드 (num, boardid) --%>
            <form:hidden path="num"/>
            <form:hidden path="boardid"/>
            
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
                
                <%-- 기존 첨부파일이 있을 경우 표시 --%>
                <c:if test="${!empty board.fileurl}">
                    <div id="file_desc" class="mb-2">
                        <a href="../file/${board.fileurl}">${board.fileurl}</a>
                        <a href="javascript:file_delete()" class="text-danger ms-2">[첨부파일 삭제]</a>
                    </div>
                </c:if>
                <form:hidden path="fileurl"/>
                
                <%-- 새 첨부파일 선택 --%>
                <input type="file" name="file1" id="file1" class="form-control">
            </div>

            <hr>

            <div class="d-flex justify-content-between">
                <a href="list?boardid=${board.boardid}" class="btn btn-secondary">
                    <i class="bi bi-list-ul"></i> 게시글 목록
                </a>
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-check-lg"></i> 게시글 수정 완료
                </button>
            </div>
            
        </form:form>
    </div>

    <%-- JavaScript 라이브러리 로드 --%>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-bs5.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.20/lang/summernote-ko-KR.min.js"></script>
    
<script type="text/javascript">
    // 첨부파일 삭제 기능
    function file_delete() {
        // 숨겨진 fileurl input의 값을 비워서 서버에 전달
        document.f.fileurl.value = "";
        // 화면에서 파일 정보 숨기기
        document.getElementById("file_desc").style.display = "none";
        alert("첨부파일이 삭제되었습니다. '게시글 수정 완료' 버튼을 눌러야 최종 반영됩니다.");
    }

    // Summernote 에디터 실행 및 이미지 업로드 콜백
    $(function() {
        $('#summernote').summernote({
            height: 300,
            lang: "ko-KR",
            placeholder: '여기에 내용을 입력하세요...',
            callbacks: {
                onImageUpload: function(images) {
                    for (let i = 0; i < images.length; i++) {
                        sendFile(images[i]);
                    }
                }
            }
        });
    });

    // Summernote 에디터 내 이미지 업로드 처리 함수
    function sendFile(file) {
        let data = new FormData();
        data.append("image", file);
        
        $.ajax({
            url: "/ajax/uploadImage", // 이미지 업로드를 처리할 컨트롤러 URL
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