<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 작성</title>

<link
	href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css"
	rel="stylesheet">
<style type="text/css">
textarea {
	resize: vertical;
}
</style>
</head>
<body class="bg-gray-100 font-sans">
	<div class="container mx-auto p-6 max-w-3xl">
		<div class="bg-white shadow-md rounded-lg p-6">
			<h2 class="text-2xl font-semibold text-gray-800 mb-6 text-center">게시글
				작성</h2>
			<form:form modelAttribute="board" action="write"
				enctype="multipart/form-data" name="f">
				<input type="hidden" name="boardid" value="${param.boardid}">
				<table class="w-full table-auto border-collapse">
					<tr class="border-b">
						<th class="p-4 w-1/5 font-medium text-gray-700 text-left">글쓴이</th>
						<td class="p-4"><form:input path="writer"
								class="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
							<div class="text-red-500 text-sm mt-1">
								<form:errors path="writer" />
							</div></td>
					</tr>
					<tr class="border-b">
						<th class="p-4 font-medium text-gray-700 text-left">비밀번호</th>
						<td class="p-4"><form:password path="pass"
								class="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
							<div class="text-red-500 text-sm mt-1">
								<form:errors path="pass" />
							</div></td>
					</tr>
					<tr class="border-b">
						<th class="p-4 font-medium text-gray-700 text-left">제목</th>
						<td class="p-4"><form:input path="title"
								class="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
							<div class="text-red-500 text-sm mt-1">
								<form:errors path="title" />
							</div></td>
					</tr>
					<tr class="border-b">
						<th class="p-4 font-medium text-gray-700 text-left">내용</th>
						<td class="p-4"><form:textarea path="content" rows="10"
								id="summernote"
								class="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />

							<div class="text-red-500 text-sm mt-1">
								<form:errors path="content" />
							</div></td>
					</tr>
					<tr class="border-b">
						<th class="p-4 font-medium text-gray-700 text-left">첨부파일</th>
						<td class="p-4"><input type="file" name="file1"
							class="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
						</td>
					</tr>
					<tr>
						<td colspan="2" class="p-4 text-center space-x-4"><a
							href="javascript:document.f.submit()"
							class="inline-block px-6 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition">게시물등록</a>
							<a href="list?boardid=${param.boardid}"
							class="inline-block px-6 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 transition">게시물목록</a>
						</td>
					</tr>
				</table>
			</form:form>
		</div>
	</div>
	<script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
	<script type="text/javascript">
		$(function() {
			$("#summernote").summernote({
				height : 300,
				callbacks:{
				//이미지업로드 이벤트발생 시 
					onImageUpload:function(images){
						for(let i=0;i<images.length;i++){
						//이미지를 여러개선택했을 경우도 생각해 배열로설정
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
				//src에 uploadImage에서 처리한 url이 넘어올거임
				success:function(src){
				//성공시 summernote에 img삽입
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