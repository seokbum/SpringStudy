<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<sitemesh:write property="title" />
<sitemesh:write property="head" />
<sitemesh:write property="body" />

<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <title><sitemesh:title default="관리자 대시보드" /></title>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <%-- [수정] 라이브러리를 Bootstrap 5와 호환되는 버전으로 정리했습니다. --%>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-bs5.min.css" rel="stylesheet">

    <style>
        body {
            overflow-x: hidden;
        }
        .sidebar {
            width: 250px;
            transition: all 0.3s ease;
            position: fixed;
            top: 56px;
            bottom: 0;
            left: 0;
            background-color: #f8f9fa;
            overflow-y: auto;
            z-index: 1000;
        }
        .sidebar.collapsed {
            width: 80px;
        }
        .main-content {
            margin-left: 250px;
            transition: all 0.3s ease;
            padding: 1.5rem;
            margin-top: 56px;
        }
        .main-content.collapsed {
            margin-left: 80px;
        }
        .sidebar .list-group-item {
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
        .footer {
            text-align: center;
            padding: 1rem;
            border-top: 1px solid #dee2e6;
            margin-top: 2rem;
        }
    </style>

    <sitemesh:head />
</head>

<body>

    <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
        <div class="container-fluid">
            <button class="btn btn-outline-light me-2" id="toggleSidebar">☰</button>
            <a class="navbar-brand" href="#">MyAdmin</a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item"><a class="nav-link" href="#">홈</a></li>
                    <li class="nav-item"><a class="nav-link" href="#">설정</a></li>
                    <a href="/user/logout" class="btn btn-outline-secondary btn-sm">로그아웃</a>
                </ul>
            </div>
        </div>
    </nav>

    <div id="sidebar" class="sidebar border-end">
        <div class="list-group list-group-flush mt-3">
            <a href="/admin/dashboard" class="list-group-item list-group-item-action">📊 대시보드</a>
            <a href="/admin/users" class="list-group-item list-group-item-action">👥 사용자 관리</a>
            <a href="/board/list?boardid=1" class="list-group-item list-group-item-action">📌 공지사항</a>
            <a href="/board/list?boardid=2" class="list-group-item list-group-item-action">💬 자유게시판</a>
            <a href="/board/list?boardid=3" class="list-group-item list-group-item-action">❓ Q&A</a>
            <a href="#" class="list-group-item list-group-item-action">⚙ 설정</a>
        </div>
    </div>

    <div id="mainContent" class="main-content">
        <sitemesh:body />
    </div>

    <footer class="footer text-muted">
        © 2025 MyAdmin. All rights reserved.

        <%-- [수정] 보내주신 구조는 그대로 두고, 부트스트랩 스타일만 적용했습니다. --%>
        <hr>
        <div class="d-flex justify-content-center gap-2 mt-3">
            <span id="si">
                <select name="si" onchange="getText('si')" class="form-select">
                    <option value="">시도를 선택하세요</option>
                </select>
            </span>
            <span id="gu">
                <select name="gu" onchange="getText('gu')" class="form-select">
                    <option value="">구군을 선택하세요</option>
                </select>
            </span>
            <span id="dong">
                <select name="dong" class="form-select">
                    <option value="">동리를 선택하세요</option>
                </select>
            </span>
        </div>
    </footer>

    <%-- [수정] JS 라이브러리를 정리하고, 오류 방지를 위한 빈 getText() 함수를 추가했습니다. --%>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-bs5.min.js"></script>

    <script>
        // 사이드바 토글 기능
        const toggleBtn = document.getElementById("toggleSidebar");
        const sidebar = document.getElementById("sidebar");
        const mainContent = document.getElementById("mainContent");

        toggleBtn.addEventListener("click", () => {
            sidebar.classList.toggle("collapsed");
            mainContent.classList.toggle("collapsed");
        });

        // onchange에서 호출할 함수가 없으면 오류가 나므로, 빈 함수를 추가했습니다.
        // 실제 기능은 이 안에 구현하시면 됩니다.
        function getText(type) {
            console.log(type + "이(가) 선택되었습니다.");
        }
    </script>

</body>
</html>