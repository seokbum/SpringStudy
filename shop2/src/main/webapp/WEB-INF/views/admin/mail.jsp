<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>메일보내기</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <!-- Summernote CSS -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote.min.css" rel="stylesheet">
    <!-- jQuery and Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Summernote JS -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote.min.js"></script>

    <style>
        body {
            background-color: #f8f9fa;
            padding-top: 20px;
        }
        .mail-form-container {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
        }
        .card {
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        .card-header {
            background-color: #007bff;
            color: white;
            border-radius: 10px 10px 0 0;
        }
        .form-group {
            margin-bottom: 1.5rem;
        }
        .form-control, .custom-select, .custom-file-input {
            border-radius: 5px;
        }
        .custom-file-label {
            border-radius: 5px;
        }
        .error {
            color: #dc3545;
            font-size: 0.875rem;
            margin-top: 0.25rem;
        }
        .btn-submit {
            background-color: #007bff;
            border-color: #007bff;
            padding: 0.5rem 2rem;
            font-size: 1rem;
            border-radius: 5px;
        }
        .btn-submit:hover {
            background-color: #0056b3;
            border-color: #0056b3;
        }
        /* Summernote 스타일 조정 */
        .note-editor.note-frame {
            border-radius: 5px;
            border: 1px solid #ced4da;
        }
        .note-toolbar {
            background-color: #f8f9fa;
            border-bottom: 1px solid #ced4da;
            border-radius: 5px 5px 0 0;
        }
        /* 반응형 조정 */
        @media (max-width: 576px) {
            .mail-form-container {
                padding: 15px;
            }
            .card-header h2 {
                font-size: 1.5rem;
            }
        }
    </style>
</head>
<body>
    <div class="mail-form-container">
        <div class="card">
            <div class="card-header">
                <h2 class="mb-0">메일 보내기</h2>
            </div>
            <div class="card-body">
                <form:form modelAttribute="mail" name="mailForm" action="mail" enctype="multipart/form-data">
                    <div class="form-group">
                        <label for="googleid">본인 구글 ID</label>
                        <form:input path="googleid" class="form-control" placeholder="구글 ID를 입력하세요" />
                        <form:errors path="googleid" cssClass="error"/>
                    </div>
                    <div class="form-group">
                        <label for="googlepw">본인 구글 비밀번호</label>
                        <input type="password" name="googlepw" class="form-control" placeholder="구글 비밀번호를 입력하세요">
                        <form:errors path="googleid" cssClass="error"/>
                    </div>
                    <div class="form-group">
                        <label>보내는 사람</label>
                        <input type="text" class="form-control" value="${loginUser.email}" readonly>
                    </div>
                    <div class="form-group">
                        <label for="recipient">받는 사람</label>
                        <form:input path="recipient" class="form-control" placeholder="받는 사람 이메일을 입력하세요"/>
                        <form:errors path="recipient" cssClass="error"/>
                    </div>
                    <div class="form-group">
                        <label for="title">제목</label>
                        <form:input path="title" class="form-control" placeholder="메일 제목을 입력하세요"/>
                        <form:errors path="title" cssClass="error"/>
                    </div>
                    <div class="form-group">
                        <label for="mtype">메시지 형식</label>
                        <select name="mtype" class="custom-select">
                            <option value="text/html; charset=UTF-8">HTML</option>
                            <option value="text/plain; charset=UTF-8">TEXT</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="file1">첨부파일 1</label>
                        <div class="custom-file">
                            <input type="file" name="file1" class="custom-file-input" id="file1">
                            <label class="custom-file-label" for="file1">파일을 선택하세요</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="file2">첨부파일 2</label>
                        <div class="custom-file">
                            <input type="file" name="file2" class="custom-file-input" id="file2">
                            <label class="custom-file-label" for="file2">파일을 선택하세요</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="contents">내용</label>
                        <form:textarea path="contents" class="form-control" id="contents" rows="10"/>
                        <form:errors path="contents" cssClass="error"/>
                    </div>
                    <div class="text-center">
                        <button type="submit" class="btn btn-submit">메일 보내기</button>
                    </div>
                </form:form>
            </div>
        </div>
    </div>

    <script>
        $(function() {
            // Summernote 초기화
            $("#contents").summernote({
                height: 300,
                placeholder: "메일로 전송할 내용을 입력해주세요",
                focus: true,
                lang: "ko-KR"
            });

            // Bootstrap custom file input 처리
            $('.custom-file-input').on('change', function() {
                let fileName = $(this).val().split('\\').pop();
                $(this).siblings('.custom-file-label').addClass('selected').html(fileName || '파일을 선택하세요');
            });
        });
    </script>
</body>
</html>