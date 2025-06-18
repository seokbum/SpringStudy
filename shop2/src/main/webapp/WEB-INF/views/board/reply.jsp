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
    <title>게시판 답글 작성</title> <%-- 제목 수정 --%>

    <%-- '게시글 작성' 페이지와 동일한 최신 버전의 Bootstrap, Bootstrap Icons CDN --%>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body>
    <div class="container mt-5 mb-5">
        <h2 class="mb-4">${boardName} 답글 등록</h2> <%-- boardName 적용 --%>

        <form:form modelAttribute="board" action="reply"  name="f">
            <%-- 답글 기능에 필요한 숨겨진 필드들 --%>
            <form:hidden path="num"/>
            <form:hidden path="boardid"/>
            <form:hidden path="grp"/>
            <form:hidden path="grplevel"/>
            <form:hidden path="grpstep"/>
            
            <div class="mb-3">
                <label class="form-label">글쓴이</label>
                <%-- 답글 작성자는 새로 입력해야 하므로 빈 칸으로 시작 --%>
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
                <%-- JSTL에 의해 'RE:'가 붙은 제목이 표시됨 --%>
                <form:input path="title" class="form-control" placeholder="제목을 입력하세요"/>
                <div class="text-danger small mt-1">
                    <form:errors path="title"/>
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label">내용</label>
                <%-- 답글 내용은 새로 입력해야 하므로 빈 칸으로 시작 --%>
                <textarea name="content" rows="10"  id="summernote" class="form-control" placeholder="내용을 입력하세요"></textarea>
                <div class="text-danger small mt-1">
                    <form:errors path="content"/>
                </div>
            </div>

            <hr>

            <div class="d-flex justify-content-between">
                <%-- 답글 작성 취소 시 원본 글로 돌아가도록 수정 --%>
                <a href="detail?num=${board.num}" class="btn btn-secondary">
                    <i class="bi bi-arrow-left"></i> 취소
                </a>
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-pencil-fill"></i> 답변글 등록
                </button>
            </div>
            
        </form:form>
    </div>
<script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
<script type="text/javascript">
		$(function() {
			$("#summernote").summernote({
				height : 300,
				callbacks:{
					onImageUpload:function(images){
						for(let i=0;i<images.length;i++){
							sendFile(images[i])
						}
					}
				}
			})
		})
		function sendFile(file){
			//new FormData() : 파일업로드를 위한 데이터 컨테이너생성
			let data = new FormData();
			data.append("image",file); //컨테이너에 이미지객체 추가
			$.ajax({ //ajax이용해 파일업로드
				url:"/ajax/uploadImage", //요청URL(ajax컨트롤러)
				type:"post",
				data:data, //전송할 데이터:컨테이너
				processData:false,
				cache:false,
				contentType:false,
				success:function(src){
					$("#summernote").summernote("insertImage",src);
				},
				error:function(e){ //서버응답오류
					alert("이미지업로드실패 : "+e.status)
				}			
			})
		}
	</script>
   
</body>
</html>