<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title><sitemesh:write property="title" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1">

<%-- summernote 관련 설정
    jquery, bootstrap 기능 사용 --%>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote.min.js"></script>

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

#exchange table th, #exchange table td {
	font-size: 0.85em;
	padding: 0.3em;
	word-wrap: break-word;
	word-break: break-all;
	vertical-align: middle;
}

#exchange table th:first-child, #exchange table td:first-child {
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
	max-width: 100%; /* 화면 너비 초과 방지 */
	min-height: 100vh; /* 최소 높이를 뷰포트 높이로 설정 */
	box-sizing: border-box;
}

.main-content::before {
	content: '';
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background-image: url('/board/image/seolseol.png'); /* 실제 이미지 경로로 변경 */
	background-size: contain; /* 이미지가 컨테이너 안에 맞게 비율 유지 */
	background-repeat: no-repeat; /* 이미지 반복 방지 */
	background-position: 20% 50%; /* 이미지를 왼쪽으로 이동 (20% 왼쪽, 50% 수직 중앙) */
	opacity: 0.8; /* 투명도 유지 */
	z-index: -1; /* 콘텐츠 뒤로 배치 */
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
/* Enhanced styles for radio buttons */
.radio-toggle .btn {
   border-radius: 0.25rem;
   padding: 0.5rem 1.5rem;
   font-size: 1rem;
   border: 1px solid #6c757d; /* Gray border for inactive state */
   background-color: #ffffff; /* White background for inactive state */
   color: #212529; /* Dark text for inactive state */
   transition: all 0.2s ease;
}

.radio-toggle .btn input[type="radio"] {
   display: none;
}

.radio-toggle .btn.active {
   background-color: #007bff; /* Blue background for active state */
   color: #ffffff; /* White text for active state */
   border-color: #007bff; /* Matching border */
   box-shadow: 0 0 5px rgba(0, 123, 255, 0.5); /* Subtle glow effect */
}

.radio-toggle .btn:hover:not(.active) {
   background-color: #e7f1ff; /* Light blue hover background */
   border-color: #0056b3; /* Darker border on hover */
   color: #0056b3; /* Darker text on hover */
}

.radio-toggle .btn:focus {
   outline: none;
   box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25); /* Focus ring */
}

/* 차트 컨테이너를 가로로 배치하기 위한 스타일 */
.chart-wrapper {
    display: flex;
    /* flex-wrap: wrap; -> 모바일 반응형 제거로 인해 불필요 */
    justify-content: space-around; /* 항목들 사이에 공간을 균등하게 분배 */
    gap: 20px; /* 차트 사이에 간격 추가 */
    margin-bottom: 2rem; /* 하단 여백 추가 */
}

/* 개별 radio-toggle div의 너비를 조정하여 차트가 나란히 놓일 공간 확보 */
.chart-wrapper > .radio-toggle {
    width: 48%; /* 두 개의 차트가 한 줄에 들어가도록 너비 조정 */
    /* 기존 radio-toggle에 있던 margin-bottom은 chart-wrapper의 gap으로 대체 */
    margin-bottom: 0;
}

#piecontainer, #barcontainer {
   width: 100%; /* 부모 .radio-toggle에 48%를 줬으므로 여기서는 100%로 채움 */
   height: 250px; /* 명시적으로 높이 설정 */
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
			<a class="navbar-brand"
				href="/user/mypage?userid=${loginUser.userid}">MyAdmin</a>
			<div class="collapse navbar-collapse">
				<ul class="navbar-nav ms-auto">
					<li class="nav-item"><a class="nav-link" href="#">${loginUser.userid}님
							하이</a></li>
					<li class="nav-item"><a class="nav-link"
						href="/user/mypage?userid=${loginUser.userid}">홈</a></li>
					<li class="nav-item"><a class="nav-link" href="#">설정</a></li>
					<li class="nav-item"><a class="nav-link" href="/user/logout">로그아웃</a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div id="sidebar" class="sidebar border-end">
		<div class="list-group list-group-flush mt-3">
			<a href="/admin/dashboard"
				class="list-group-item list-group-item-action">📊 대시보드</a> <a
				href="/admin/users" class="list-group-item list-group-item-action">👥
				사용자 관리</a> <a href="/board/list?boardid=1"
				class="list-group-item list-group-item-action">📌 공지사항</a> <a
				href="/board/list?boardid=2"
				class="list-group-item list-group-item-action">💬 자유게시판</a> <a
				href="/board/list?boardid=3"
				class="list-group-item list-group-item-action">❓ Q&A</a> <a href="#"
				class="list-group-item list-group-item-action">⚙️ 설정</a>
				<a href="/chat/chat"
				class="list-group-item list-group-item-action">&#x1F917; 채팅</a>
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
                <label class="btn btn-outline-primary active"> <input
                   type="radio" name="pie" onchange="piegraph(2)" checked>
                   자유게시판
                </label> <label class="btn btn-outline-primary"> <input type="radio"
                   name="pie" onchange="piegraph(3)"> Q&A
                </label>
             </div>
             <div id="piecontainer"></div>
          </div>
          <div class="radio-toggle">
             <div class="btn-group" role="group" aria-label="Pie chart selection">
                <label class="btn btn-outline-primary active"> <input
                   type="radio" name="barline" onchange="barlinegraph(2)" checked>
                   자유게시판
                </label> <label class="btn btn-outline-primary"> <input type="radio"
                   name="barline" onchange="barlinegraph(3)"> Q&A
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
			© 2025 MyAdmin. All <span id="si"> <select name="si"
				onchange="getText('si')">
					<option value="">시도를 선택하세요</option>
			</select>
			</span> <span id="gu"> <select name="gu" onchange="getText('gu')">
					<option value="">구군을 선택하세요</option>
			</select>
			</span> <span id="dong"> <select name="dong">
					<option value="">동리를 선택하세요</option>
			</select>
			</span>
		</div>

	</footer>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.bundle.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.js"></script>
	<script>
    const toggleBtn = document.getElementById("toggleSidebar");
    const sidebar = document.getElementById("sidebar");
    const mainContent = document.getElementById("mainContent");

    toggleBtn.addEventListener("click", () => {
      sidebar.classList.toggle("collapsed");
      mainContent.classList.toggle("collapsed");
    });
    
    $(function(){
      getSido1();
      //exchangeRate(); //수출입은행 환율정보조회 서버에서 HTML형식(문자열)으로 리턴
      exchangeRate2();   //서버에서 Map형식(JSON)으로 리턴
      piegraph(2) //글쓴이별 게시글 건수를 파이그래프로출력
      boardImg();
    })
    
    function getSido1(){//문자열 형식
       $.ajax({
         url:"/ajax/select1",
         success:function(data){
            //성공시 컨트롤러에서반환하는  list.toString을 가져옴
            console.log(data)
            let arr=data.substring
               (data.indexOf('[')+1, data.indexOf(']')).split(",");
         $.each(arr,function(i,item){
               $("select[name='si']").append(function(){
                  return "<option>"+item+"</option>"
               })
            })
         }
       })
    }
    
    function getText(name){
      let city = $("select[name='si']").val();
      let gu = $("select[name='gu']").val();
      let disname;
      //최상단에 뜰 text
      let toptext="구군을 선택하세요";
      let params="";
      
      if(name=="si"){
         params = "si="+city.trim();
         disname="gu";
      }
      else if(name=="gu"){
         params="si="+city.trim()+"&gu="+gu.trim();
         disname="dong";
         toptext="동리를 선택하세요";   
      }
      else{
         return;
      }
      $.ajax({
            url:"/ajax/select2",
            type:"POST",
            data:params, //params를  select2로 보냄
            success:function(arr){
               //서버에서 배열 객체로 전달받음
                  console.log(arr);
               $("select[name="+disname+"] option").remove();
               $("select[name="+disname+"]").append(function(){
                  return "<option value=''>"+toptext+"</option>"
               })
               
               $.each(arr,function(i,item){
                  //arr에서 클릭 한 카테고리에맞는 item을 모두뽑음
                  $("select[name="+disname+"]").append(function(){
                     return "<option>"+item+"</option>"
                  })
               })
               
            }
          })
    }
    function exchangeRate(){
      $.ajax("/ajax/exchange1",{
         success: function(data){
            console.log(data);
            $("#exchange").html(data)
         },
         error:function(e){
            alert("환율조회시 서버오류발생",e.status)
         }
      })
    }
    
    //GOOD
    function exchangeRate2(){ 
      $.ajax("/ajax/exchange2",{ 
         success: function(json){ //서버Map객체로 전송 , 클라이언트 : JSON형식
            console.log(json);
            let html = "<h4 class='text-center my-3'>수출입은행<br>"+json.exdate +"</h4>";
            html += "<table class='table table-bordered table-hover text-center'>";
            html += "<thead><tr><th>통화</th><th>기준율</th><th>받으실때</th>"
                  +"<th>보내실때</th></tr></thead>";
         //json.trlist : 4개의 배열객체 저장                  
            $.each(json.trlist,function(i,tds){
               html += "<tr><td>"+tds[0]+"<br>"+tds[1]+"</td><td>"+tds[4]+"</td>"
               +"<td>"+tds[2]+"</td><td>"+tds[3]+"</td></tr>"
            })
            html += "</table>"
            $("#exchange").html(html);
         },
         error:function(e){
            alert("환율조회시 서버오류발생",e.status)
         }
      })
    }
    
    function boardImg(){
		$.ajax({
			url:"/ajax/boardImg",
			success:function(json){
				console.log(json);
				let html = "<img src='"+json.img+"'>"
				
				$("#boardImg").html(html);
			},
		error:function(e){
			alert("boardImg 오류발생 : ",e.status);
				}
			})
    }
    
    
    let randomColorFactor = function(){
      return Math.round(Math.random()*255)
    }
    
    let randomColor = function(opa){
      return "rgba("+randomColorFactor()+","   
      +randomColorFactor()+","   
      +randomColorFactor()+","   
      +(opa || ".3") + ")";
    } //각각의 색깔
    
    function piegraph(id){
      $.ajax("/ajax/graph1?id="+id,{
         success : function(json){
            // 캔버스 생성 시 높이 속성 추가
            let canvas = "<canvas id='canvas1' style='width:100%; height:100%;'></canvas>"; // 캔버스 높이 직접 지정 (예: 100px)
            $("#piecontainer").html(canvas);
            pieGraphPrint(json,id);
         },
         error : function(e){
            alert("서버오류 : "+e.status);
         }
      })
    }
    
    function pieGraphPrint(arr,id){
//arr : [{장원영:10},{안유진:20},....]
      let colors = [];
      let writers = [];
      let datas = [];
      $.each(arr,function(index){
         colors[index] = randomColor(0.5);
         for(key in arr[index]){
            writers.push(key); // key값(writer)
            datas.push(arr[index][key]); //각map의 value(cnt)
         }
      })
      let title = (id == 2)?"자유게시판":"Q&A";
      let config = {
         type:'pie',
         data : {
            datasets : [{ data:datas,
               backgroundColor : colors}],
            labels : writers
         },
         options : {
            responsive : true,
            maintainAspectRatio: false, // 가로-세로 비율 유지를 비활성화합니다.
            legend : {
                display:true,
                position:"right",
                labels: {
                    boxWidth: 15, // 범례 상자의 너비를 더 줄입니다.
                    padding: 5   // 범례 항목 간의 간격을 더 줄입니다.
                }
            },
            title : {
               display : true,
               text : '글쓴이 별 '+title+"등록건수",
               position : 'bottom',
               fontSize: 10 // 제목 글씨 크기를 더 줄입니다.
            },
            layout: {
                padding: {
                    left: 5, // 그래프 내부 여백을 더 줄입니다.
                    right: 5,
                    top: 5,
                    bottom: 5
                }
            },
            elements: {
                arc: {
                    borderWidth: 0 // 조각의 테두리를 제거하여 시각적으로 더 작게 보이게 할 수 있습니다.
                }
            }
         }
      }
      let ctx = document.getElementById("canvas1")
      new Chart(ctx,config)      
    }
    
    
    function barlinegraph(id){
        $.ajax("/ajax/graph2?id="+id,{
           success : function(arr){
              // 캔버스 생성 시 높이 속성 추가
              let canvas = "<canvas id='canvas2' style='width:100%; height:100%;'></canvas>"; // 캔버스 높이 직접 지정 (예: 100px)
              $("#barcontainer").html(canvas);
              barlinGraphPrint(arr,id);
           },
           error : function(e){
              alert("서버오류 : "+e.status);
           }
        })
      }
    
    function barlinGraphPrint(arr,id){
    	let colors = [];
        let regdates = [];
        let datas = [];
        $.each(arr,function(index){
			colors[index] = randomColor(0.5)
			for(key in arr[index]){
				regdates.push(key);
				datas.push(arr[index][key])
			}
        })
			let title = (id==2)?"자유게시판":"Q&A";
			let config = {
				type:'bar',
				data:{
					datasets:[
						{
							type:"line",
							borderWidth:2,
							borderColor:colors,
							label:'건수',
							fill:false,
							data:datas
						},
						{
							type:"bar",
							backgroundColor:colors,
							label : '건수',
							data : datas
						}
					],
					labels : regdates,
				},
				options : {
					responsive : true,
					legend : {display:false},
					title:{
						display :true,
						text : '최근7일'+title+"등록건수",
						position : 'bottom'
					},
					scales:{
						xAxes : [{ display : true,
							scaleLabel : {
								display : true,
								labelString : "작성일자"
							}
						}],
						yAxes : [{
							scaleLabel : {
								display : true,
								labelString : "게시물 등록 건수"
							},
							ticks : {begenAtZero : true}
						}]
						
					}
				}
			}
			let ctx = document.getElementById("canvas2")
			new Chart(ctx,config)
    }
  </script>
</body>
</html>