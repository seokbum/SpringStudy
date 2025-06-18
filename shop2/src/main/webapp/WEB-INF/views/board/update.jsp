<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 수정</title>
<link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
<style type="text/css">
textarea {
    resize: vertical;
}
</style>
</head>
<body class="bg-gray-50 font-sans">
    <div class="container mx-auto p-6 max-w-3xl">
        <div class="bg-white shadow-sm rounded-lg p-6">
            <h2 class="text-xl font-semibold text-gray-800 mb-6 text-center">게시글 수정</h2>
            <form:form modelAttribute="board" action="update"
                enctype="multipart/form-data" name="f">
                <form:hidden path="num"/>
                <form:hidden path="boardid"/>
                <table class="w-full table-auto border-collapse">
                    <tr class="border-b">
                        <td class="p-3 w-1/5 font-medium text-gray-600">글쓴이</td>
                        <td class="p-3">
                            <form:input path="writer" class="w-full p-2 border rounded focus:outline-none focus:ring-1 focus:ring-blue-400"/>
                            <div class="text-red-500 text-sm mt-1"><form:errors path="writer"/></div>
                        </td>
                    </tr>
                    <tr class="border-b">
                        <td class="p-3 font-medium text-gray-600">비밀번호</td>
                        <td class="p-3">
                            <form:password path="pass" class="w-full p-2 border rounded focus:outline-none focus:ring-1 focus:ring-blue-400"/>
                            <div class="text-red-500 text-sm mt-1"><form:errors path="pass"/></div>
                        </td>
                    </tr>
                    <tr class="border-b">
                        <td class="p-3 font-medium text-gray-600">제목</td>
                        <td class="p-3">
                            <form:input path="title" class="w-full p-2 border rounded focus:outline-none focus:ring-1 focus:ring-blue-400"/>
                            <div class="text-red-500 text-sm mt-1"><form:errors path="title"/></div>
                        </td>
                    </tr>
                    <tr class="border-b">
                        <td class="p-3 font-medium text-gray-600">내용</td>
                        <td class="p-3">
                            <form:textarea path="content" rows="10" id="summernote" class="w-full p-2 border rounded focus:outline-none focus:ring-1 focus:ring-blue-400"/>
                            <div class="text-red-500 text-sm mt-1"><form:errors path="content"/></div>
                        </td>
                    </tr>
                    <tr class="border-b">
                        <td class="p-3 font-medium text-gray-600">첨부파일</td>
                        <td class="p-3">
                            <c:if test="${!empty board.fileurl}">
                                <div id="file_desc" class="flex items-center space-x-2">
                                    <a href="file/${board.fileurl}" class="text-blue-500 hover:underline">${board.fileurl}</a>
                                    <a href="javascript:file_delete()" class="text-red-500 hover:underline">[첨부파일 삭제]</a>
                                </div>
                            </c:if>
                            <form:hidden path="fileurl"/>
                            <input type="file" name="file1" class="mt-2 w-full p-2 border rounded focus:outline-none focus:ring-1 focus:ring-blue-400"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" class="p-3 text-center space-x-3">
                            <a href="javascript:document.f.submit()" class="inline-block px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 transition">게시글 수정</a>
                            <a href="list" class="inline-block px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 transition">게시글목록</a>
                        </td>
                    </tr>
                </table>
            </form:form>
        </div>
    </div>
<script type="text/javascript">
function file_delete(){
    document.f.fileurl.value="";
    file_desc.style.display = "none";
}

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