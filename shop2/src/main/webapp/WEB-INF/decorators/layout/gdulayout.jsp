<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <title>
        <sitemesh:write property="title" />
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <%-- summernote 관련 설정 jquery, bootstrap 기능 사용 --%>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote.min.js"></script>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

    <style>
        body {
            overflow-x: hidden;
        }

        #exchange {
            width: 95%;
            margin: 6px auto;
            overflow-x: hidden;
            box-sizing: border-box;
        }

        #exchange table {
            width: 100%;
            table-layout: fixed;
        }

        #exchange table th,
        #exchange table td {
            font-size: 0.85em;
            padding: 0.3em;
            word-wrap: break-word;
            word-break: break-all;
            vertical-align: middle;
        }

        #exchange table th:first-child,
        #exchange table td:first-child {
            width: 30%;
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
            position: relative;
            max-width: 100%;
            min-height: 100vh;
            box-sizing: border-box;
        }

        .main-content::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-image: url('/board/image/seolseol.png');
            background-size: contain;
            background-repeat: no-repeat;
            background-position: 20% 50%;
            opacity: 0.8;
            z-index: -1;
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

        /* Enhanced styles for radio buttons */
        .radio-toggle .btn {
            border-radius: 0.25rem;
            padding: 0.5rem 1.5rem;
            font-size: 1rem;
            border: 1px solid #6c757d;
            background-color: #ffffff;
            color: #212529;
            transition: all 0.2s ease;
        }

        .radio-toggle .btn input[type="radio"] {
            display: none;
        }

        .radio-toggle .btn.active {
            background-color: #007bff;
            color: #ffffff;
            border-color: #007bff;
            box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
        }

        .radio-toggle .btn:hover:not(.active) {
            background-color: #e7f1ff;
            border-color: #0056b3;
            color: #0056b3;
        }

        .radio-toggle .btn:focus {
            outline: none;
            box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
        }

        /* 차트 컨테이너를 가로로 배치하기 위한 스타일 */
        .chart-wrapper {
            display: flex;
            justify-content: space-around;
            gap: 20px;
            margin-bottom: 2rem;
        }

        /* 개별 radio-toggle div의 너비를 조정하여 차트가 나란히 놓일 공간 확보 */
        .chart-wrapper>.radio-toggle {
            flex: 1;
            min-width: 300px;
            max-width: 48%;
            box-sizing: border-box;
        }

        #piecontainer,
        #barcontainer {
            width: 100%;
            height: 250px;
            border: 1px solid #dee2e6;
            border-radius: 0.25rem;
            padding: 0.5rem;
            box-sizing: border-box;
        }
    </style>

    <sitemesh:write property="head" />
</head>

<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
        <div class="container-fluid">
            <button class="btn btn-outline-light me-2" id="toggleSidebar">☰</button>
            <a class="navbar-brand" href="/user/mypage?userid=${loginUser.userid}">MyAdmin</a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item"><a class="nav-link" href="#">${loginUser.userid}님 하이</a></li>
                    <li class="nav-item"><a class="nav-link" href="/user/mypage?userid=${loginUser.userid}">홈</a></li>
                    <li class="nav-item"><a class="nav-link" href="#">설정</a></li>
                    <li class="nav-item"><a class="nav-link" href="/user/logout">로그아웃</a></li>
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
        <a href="#" class="list-group-item list-group-item-action">⚙️ 설정</a>
        <a href="/chat/chat" class="list-group-item list-group-item-action">
            <i class="fas fa-comments"></i>&nbsp;채팅 </a>
        <a href="/chat/chatbot" class="list-group-item list-group-item-action">
            <i class="fas fa-robot"></i>&nbsp;챗봇
        </a>
        <a href="/naver/search" class="list-group-item list-group-item-action">
            <i class="fas fa-search"></i>&nbsp;네이버 검색 </a>
    </div>
</div>
        <%--수출입은행 환율정보표시영역 --%>
        <div style="width: 100%">
            <div id="exchange" style="width: 95%; margin: 6px;"></div>
        </div>
    </div>

    <div id="mainContent" class="main-content">
        <div class="chart-wrapper">
            <div class="radio-toggle">
                <div class="btn-group" role="group" aria-label="Pie chart selection">
                    <label class="btn btn-outline-primary active"> 
                        <input type="radio" name="pie" onchange="piegraph(2)" checked>
                        자유게시판
                    </label> 
                    <label class="btn btn-outline-primary"> 
                        <input type="radio" name="pie" onchange="piegraph(3)"> Q&A
                    </label>
                </div>
                <div id="piecontainer"></div>
            </div>
            <div class="radio-toggle">
                <div class="btn-group" role="group" aria-label="Bar line chart selection"> 
                    <label class="btn btn-outline-primary active"> 
                        <input type="radio" name="barline" onchange="barlinegraph(2)" checked>
                        자유게시판
                    </label> 
                    <label class="btn btn-outline-primary"> 
                        <input type="radio" name="barline" onchange="barlinegraph(3)"> Q&A
                    </label>
                </div>
                <div id="barcontainer"></div>
            </div>
        </div>
        <sitemesh:write property="body" />
    </div>

    <footer class="footer text-muted">
        <div id="boardImg"></div>
        <hr>
        <div class="d-flex justify-content-center gap-2 mt-3">
            © 2025 MyAdmin. All 
            <span id="si"> 
                <select name="si" onchange="getText('si')">
                    <option value="">시도를 선택하세요</option>
                </select>
            </span> 
            <span id="gu"> 
                <select name="gu" onchange="getText('gu')">
                    <option value="">구군을 선택하세요</option>
                </select>
            </span> 
            <span id="dong"> 
                <select name="dong">
                    <option value="">동리를 선택하세요</option>
                </select>
            </span>
        </div>

    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.bundle.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.js"></script>
    <script>
        const toggleBtn = document.getElementById("toggleSidebar");
        const sidebar = document.getElementById("sidebar");
        const mainContent = document.getElementById("mainContent");

        toggleBtn.addEventListener("click", () => {
            sidebar.classList.toggle("collapsed");
            mainContent.classList.toggle("collapsed");
        });

        $(function () {
            getSido1();
            exchangeRate2();   
            piegraph(2); 
            boardImg();
        });

        function getSido1() {
            $.ajax({
                url: "/ajax/select1",
                success: function (data) {
                    console.log(data);
                    let arr = data.substring
                        (data.indexOf('[') + 1, data.indexOf(']')).split(",");
                    $.each(arr, function (i, item) {
                        $("select[name='si']").append(function () {
                            return "<option>" + item + "</option>";
                        });
                    });
                }
            });
        }

        function getText(name) {
            let city = $("select[name='si']").val();
            let gu = $("select[name='gu']").val();
            let disname;
            let toptext = "구군을 선택하세요";
            let params = "";

            if (name == "si") {
                params = "si=" + city.trim();
                disname = "gu";
            }
            else if (name == "gu") {
                params = "si=" + city.trim() + "&gu=" + gu.trim();
                disname = "dong";
                toptext = "동리를 선택하세요";
            }
            else {
                return;
            }
            $.ajax({
                url: "/ajax/select2",
                type: "POST",
                data: params,
                success: function (arr) {
                    console.log(arr);
                    $("select[name=" + disname + "] option").remove();
                    $("select[name=" + disname + "]").append(function () {
                        return "<option value=''>" + toptext + "</option>";
                    });

                    $.each(arr, function (i, item) {
                        $("select[name=" + disname + "]").append(function () {
                            return "<option>" + item + "</option>";
                        });
                    });

                }
            });
        }
        function exchangeRate() { 
            $.ajax("/ajax/exchange1", {
                success: function (data) {
                    console.log(data);
                    $("#exchange").html(data);
                },
                error: function (e) {
                    alert("환율조회시 서버오류발생", e.status);
                }
            });
        }

        //GOOD
        function exchangeRate2() {
            $.ajax("/ajax/exchange2", {
                success: function (json) { 
                    console.log(json);
                    let html = "<h4 class='text-center my-3'>수출입은행<br>" + json.exdate + "</h4>";
                    html += "<table class='table table-bordered table-hover text-center'>";
                    html += "<thead><tr><th>통화</th><th>기준율</th><th>받으실 때</th>"
                        + "<th>보내실 때</th></tr></thead>"; 
                    $.each(json.trlist, function (i, tds) {
                        html += "<tr><td>" + tds[0] + "<br>" + tds[1] + "</td><td>" + tds[4] + "</td>"
                            + "<td>" + tds[2] + "</td><td>" + tds[3] + "</td></tr>";
                    });
                    html += "</table>";
                    $("#exchange").html(html);
                },
                error: function (e) {
                    alert("환율조회시 서버오류발생", e.status);
                }
            });
        }

        function boardImg() {
            $.ajax({
                url: "/ajax/boardImg",
                success: function (json) {
                    console.log(json);
                    let html = "<img src='" + json.img + "'>";

                    $("#boardImg").html(html);
                },
                error: function (e) {
                    alert("boardImg 오류발생 : ", e.status);
                }
            });
        }


        let randomColorFactor = function () {
            return Math.round(Math.random() * 255);
        };

        let randomColor = function (opa) {
            return "rgba(" + randomColorFactor() + ","
                + randomColorFactor() + ","
                + randomColorFactor() + ","
                + (opa || ".3") + ")";
        }; 

        function piegraph(id) {
            $.ajax("/ajax/graph1?id=" + id, {
                success: function (json) {
                    let canvas = "<canvas id='canvas1' style='width:100%; height:100%;'></canvas>";
                    $("#piecontainer").html(canvas);
                    pieGraphPrint(json, id);
                },
                error: function (e) {
                    alert("서버오류 : " + e.status);
                }
            });
        }

        function pieGraphPrint(arr, id) {
            let colors = [];
            let writers = [];
            let datas = [];
            $.each(arr, function (index) {
                colors[index] = randomColor(0.5);
                for (let key in arr[index]) { 
                    writers.push(key);
                    datas.push(arr[index][key]);
                }
            });
            let title = (id == 2) ? "자유게시판" : "Q&A";
            let config = {
                type: 'pie',
                data: {
                    datasets: [{
                        data: datas,
                        backgroundColor: colors
                    }],
                    labels: writers
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    legend: {
                        display: true,
                        position: "right",
                        labels: {
                            boxWidth: 15,
                            padding: 5
                        }
                    },
                    title: {
                        display: true,
                        text: '글쓴이 별 ' + title + "등록건수", 
                        position: 'bottom',
                        fontSize: 10
                    },
                    layout: {
                        padding: {
                            left: 5,
                            right: 5,
                            top: 5,
                            bottom: 5
                        }
                    },
                    elements: {
                        arc: {
                            borderWidth: 0
                        }
                    }
                }
            };
            let ctx = document.getElementById("canvas1");
            new Chart(ctx, config);
        }


        function barlinegraph(id) {
            $.ajax("/ajax/graph2?id=" + id, {
                success: function (arr) {
                    let canvas = "<canvas id='canvas2' style='width:100%; height:100%;'></canvas>";
                    $("#barcontainer").html(canvas);
                    barlinGraphPrint(arr, id); 
                },
                error: function (e) {
                    alert("서버오류 : " + e.status);
                }
            });
        }

        function barlinGraphPrint(arr, id) { 
            let colors = [];
            let regdates = [];
            let datas = [];
            $.each(arr, function (index) {
                colors[index] = randomColor(0.5);
                for (let key in arr[index]) { 
                    regdates.push(key);
                    datas.push(arr[index][key]);
                }
            });
            let title = (id == 2) ? "자유게시판" : "Q&A";
            let config = {
                type: 'bar',
                data: {
                    datasets: [
                        {
                            type: "line",
                            borderWidth: 2,
                            borderColor: colors,
                            label: '건수',
                            fill: false,
                            data: datas
                        },
                        {
                            type: "bar",
                            backgroundColor: colors,
                            label: '건수',
                            data: datas
                        }
                    ],
                    labels: regdates,
                },
                options: {
                    responsive: true,
                    legend: { display: false },
                    title: {
                        display: true,
                        text: '최근 7일 ' + title + " 등록건수", 
                        position: 'bottom'
                    },
                    scales: {
                        xAxes: [{
                            display: true,
                            scaleLabel: {
                                display: true,
                                labelString: "작성일자"
                            }
                        }],
                        yAxes: [{
                            scaleLabel: {
                                display: true,
                                labelString: "게시물 등록 건수"
                            },
                            ticks: { beginAtZero: true } 
                        }]

                    }
                }
            };
            let ctx = document.getElementById("canvas2");
            new Chart(ctx, config);
        }
    </script>
</body>

</html>