<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- 폼을 그리기 전, JSTL을 이용해 제목에 'RE:'를 미리 붙여줍니다. --%>
<c:if test="${!fn:startsWith(board.title, 'RE:')}">
   <c:set target="${board}" property="title" value="RE: ${board.title}" />
</c:if>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>게시판 답글 작성</title>
    
    <%-- Bootstrap CSS 및 아이콘 --%>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    
    <%-- Summernote CSS 추가 --%>
    <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-bs5.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5 mb-5">
        <h2 class="mb-4">${boardName} 답글 등록</h2>

        <form:form modelAttribute="board" action="reply">
            <%-- 답글 기능에 필요한 숨겨진 필드들 --%>
            <form:hidden path="num"/>
            <form:hidden path="boardid"/>
            <form:hidden path="grp"/>
            <form:hidden path="grplevel"/>
            <form:hidden path="grpstep"/>
            
            <div class="mb-3">
                <form:label path="writer" class="form-label">글쓴이</form:label>
                <%-- 답글 작성자는 새로 입력해야 하므로 form:input 대신 일반 input 사용 --%>
                <input type="text" name="writer" class="form-control" placeholder="작성자 이름을 입력하세요"/>
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
                <%-- 답글 내용은 새로 입력하므로 form:textarea 대신 일반 textarea 사용하고 id 적용 --%>
                <textarea name="content" rows="10" class="form-control" id="summernote"></textarea>
                <div class="text-danger small mt-1">
                    <form:errors path="content"/>
                </div>
            </div>

            <hr>

            <div class="d-flex justify-content-between">
                <a href="detail?num=${board.num}" class="btn btn-secondary">
                    <i class="bi bi-arrow-left"></i> 취소
                </a>
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-pencil-fill"></i> 답변글 등록
                </button>
            </div>
        </form:form>
    </div>

    <%-- jQuery 풀 버전, Bootstrap JS, Summernote JS 순서로 로드 --%>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-bs5.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.20/lang/summernote-ko-KR.min.js"></script>

    <script type="text/javascript">
    	$(function() {
			$('#summernote').summernote({
				height : 300,
                lang: "ko-KR",
                placeholder: '여기에 내용을 입력하세요...',
				callbacks : {
					onImageUpload : function(images){
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
    			url : "/ajax/uploadImage",
    			type : "post",
    			data : data,
    			processData : false,
    			contentType : false,
    			success : function(src) {
    				$("#summernote").summernote("insertImage", src);
    			},
    			error : function(e) {
    				alert("이미지 업로드에 실패했습니다: " + e.status);
    			}
    		});
    	}
    </script>
</body>
</html>