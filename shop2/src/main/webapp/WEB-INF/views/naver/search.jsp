<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>네이버 검색</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom CSS for additional styling -->
    <style>
        body {
            background-color: #f8f9fa;
        }
        .search-card {
            max-width: 800px;
            margin: 2rem auto;
        }
        .table img {
            object-fit: cover;
        }
        .pagination .page-link {
            color: #0d6efd;
        }
        .pagination .page-item.active .page-link {
            background-color: #0d6efd;
            border-color: #0d6efd;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="card shadow search-card">
            <div class="card-body">
                <h3 class="card-title text-center mb-4">네이버 검색</h3>
                <form class="row g-3 align-items-center mb-4" onsubmit="event.preventDefault(); naversearch(1);">
                    <div class="col-md-3">
                        <select id="type" class="form-select">
                            <option value="blog">블로그</option>
                            <option value="news">뉴스</option>
                            <option value="book">책</option>
                            <option value="encyc">백과사전</option>
                            <option value="cafeArticle">카페글</option>
                            <option value="kin">지식인</option>
                            <option value="local">지역</option>
                            <option value="webkr">웹문서</option>
                            <option value="image">이미지</option>
                            <option value="shop">쇼핑</option>
                            <option value="doc">전문자료</option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <select id="display" class="form-select">
                            <option value="10">10</option>
                            <option value="20">20</option>
                            <option value="50">50</option>
                        </select>
                    </div>
                    <div class="col-md-4">
                        <input type="text" id="data" class="form-control" placeholder="검색어를 입력하세요">
                    </div>
                    <div class="col-md-2">
                        <button type="submit" class="btn btn-primary w-100">검색</button>
                    </div>
                </form>
                <div id="result"></div>
            </div>
        </div>
    </div>

    <!-- jQuery (required for existing AJAX functionality) -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function naversearch(start) {
            $.ajax({
                type: "POST",
                url: "naversearch",
                data: {
                    data: $("#data").val(),
                    display: $("#display").val(),
                    start: start,
                    type: $("#type").val(),
                },
                success: function(json) {
                    let total = json.total; //검색 총 건수
                    let html = "";
                    let num = (start - 1) * parseInt($("#display").val()) + 1;
                    let maxpage = Math.ceil(total / parseInt($("#display").val()));
                    let startpage = (Math.ceil(start / 10) - 1) * 10 + 1;
                    let endpage = startpage + 9;
                    if (endpage > maxpage) {
                        endpage = maxpage;
                    }

                    // Result table
                    html += "<table class='table table-hover'><thead><tr><th scope='col'>#</th><th scope='col'>제목</th>";
                    if ($("#type").val() === 'image') {
                        html += "<th scope='col'>이미지</th>";
                    } else {
                        html += "<th scope='col'>링크</th><th scope='col'>설명</th>";
                    }
                    html += "</tr></thead><tbody>";
                    $.each(json.items, function(i, item) {
                        html += "<tr><td>" + num + "</td><td>" + item.title + "</td>";
                        if ($("#type").val() === 'image') {
                            html += "<td><a href='" + item.link + "' target='_blank'><img src='" + item.thumbnail + "' alt='thumbnail' class='img-fluid' style='max-width: 100px; max-height: 100px;'></a></td>";
                        } else {
                            html += "<td><a href='" + item.link + "' target='_blank' class='text-primary'>" + item.link + "</a></td><td>" + item.description + "</td>";
                        }
                        html += "</tr>";
                        num++;
                    });
                    html += "</tbody></table>";

                    // Pagination
                    html += "<div class='d-flex justify-content-between align-items-center mt-3'>";
                    html += "<span>전체 조회 건수: " + total + " | 현재 페이지: " + start + "/" + maxpage + "</span>";
                    html += "<nav><ul class='pagination mb-0'>";
                    if (start > 1) {
                        html += "<li class='page-item'><a class='page-link' href='javascript:naversearch(" + (start - 1) + ")'>이전</a></li>";
                    }
                    for (let i = startpage; i <= endpage; i++) {
                        html += "<li class='page-item" + (i === start ? " active" : "") + "'><a class='page-link' href='javascript:naversearch(" + i + ")'>" + i + "</a></li>";
                    }
                    if (maxpage > start) {
                        html += "<li class='page-item'><a class='page-link' href='javascript:naversearch(" + (start + 1) + ")'>다음</a></li>";
                    }
                    html += "</ul></nav></div>";

                    $("#result").html(html);
                },
                error: function(xhr, status, error) {
                    $("#result").html("<div class='alert alert-danger'>오류 발생: " + xhr.status + " " + error + "</div>");
                }
            });
        }
    </script>
</body>
</html>