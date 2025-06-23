<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>AI 챗봇</title>
        <link rel="stylesheet" href="/css/chatbot.css">
    </head>

    <body>
        <div id="chatbotarea" class="text-center rounded" style="background-color: #1A6511;">
            <label for="gptarea" class="text-light fw-bold">AI챗봇</label>
            <div id="gptanswerarea" class="chatbot" style="background-color: #A8c0d6;">
                <div class="chat bot">
                    <div class="icon">
                        <i></i>
                    </div>
                    <div class="textbox text-start">AI 챗봇입니다. 궁금하신 점을 문의해주세요</div>
                </div>
            </div>
        </div>
        <hr>
        <div id="gptarea">
                <textarea id="gpt_question" rows="2" class="form-control"></textarea>
        </div>
        <div class="text-end d-flex justify-content-between">
            <button class="btn btn-primary text-light" onclick="gptquestion()">AI문의</button>
        </div>
        <br>
        <script>
            let gpt_question_input = document.querySelector("#gpt_question"); 
            gpt_question_input.addEventListener("keydown",(e)=>{
                if(e.keyCode == 13) { 
                    e.preventDefault(); 
                    gptquestion();
                }
            });

            function gptquestion() {
                let question_text = gpt_question_input.value; 
                if(question_text == ''){
                    alert("AI 챗봇에게 질문할 내용을 입력해 주세요");
                    gpt_question_input.focus();
                    return;
                }
                if(question_text.length < 10) {
                    alert("AI 챗봇에게 질문할 내용을 좀 더 자세히 입력해 주세요");
                    gpt_question_input.focus();
                    return;
                }
                
                let html_user = "<div class ='chat me'><div class='icon'><i></i></div>"; 
                html_user += "<div class = 'textbox'>"+question_text+"</div></div>";
                document.querySelector("#gptanswerarea").innerHTML += html_user;
                gpt_question_input.value = "";
                
                let gptanswerarea = document.querySelector("#gptanswerarea");
                gptanswerarea.scrollTop = gptanswerarea.scrollHeight; 

                
                let paramdata = {
                    method : "POST",
                    headers : {"Content-Type":"application/x-www-form-urlencoded;charset=UTF-8"},
                    cache : "no-cache",
                    referrerPolicy:"no-referrer", 
                    body : "question=" + encodeURIComponent(question_text) 
                };
				// jquery 방식이 아니고,ES6 버전으로 서버에 요청
                fetch('/ajax/gptquestion',paramdata) 
                    .then(response => response.text())
                    .then(gptdata=>{
                        let html_bot = "<div class='chat bot'><div class='icon'><i></i></div>";
                        html_bot += "<div class='textbox text-start'>" + gptdata.replaceAll("\n","<br>").replaceAll(" ", "&nbsp;") + "</div></div>";
                        
                        document.querySelector("#gptanswerarea").innerHTML += html_bot;
                        
                        let gptanswerarea = document.querySelector("#gptanswerarea");
                        gptanswerarea.scrollTop = gptanswerarea.scrollHeight; 
                    })
                    .catch(error => { 
                        console.error('Fetch Error:', error);
                        let html_error = "<div class='chat bot'><div class='icon'><i></i></div>";
                        html_error += "<div class='textbox text-start' style='color: red;'>AI 응답을 가져오는 중 오류가 발생했습니다.</div></div>";
                        document.querySelector("#gptanswerarea").innerHTML += html_error;
                        let gptanswerarea = document.querySelector("#gptanswerarea");
                        gptanswerarea.scrollTop = gptanswerarea.scrollHeight;
                    });
            }
        </script>
    </body>

    </html>