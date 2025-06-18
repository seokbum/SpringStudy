```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="port" value="${pageContext.request.localPort}"/>
<c:set var="server" value="${pageContext.request.serverName}"/>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>WebSocket Chat</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<style>
    body {
        background: linear-gradient(135deg, #f3e7e9, #d4f1f4);
        font-family: 'Arial', sans-serif;
        display: flex;
        justify-content: center;
        align-items: center;
        min-height: 100vh;
        margin: 0;
        overflow: hidden;
    }
    .chat-container {
        background: #fff;
        border-radius: 20px;
        box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
        width: 90%;
        max-width: 500px;
        height: 80vh;
        display: flex;
        flex-direction: column;
        overflow: hidden;
    }
    #chatStatus {
        background: #e0e0e0;
        color: #333;
        font-size: 0.9em;
        padding: 8px 15px;
        text-align: center;
        border-bottom: 1px solid #ddd;
    }
    .chat-messages {
        flex: 1;
        padding: 20px;
        overflow-y: auto;
        scroll-behavior: smooth;
        background: #f9f9f9;
    }
    .message {
        margin: 8px 0;
        padding: 12px 18px;
        border-radius: 15px;
        max-width: 70%;
        word-wrap: break-word;
        font-size: 0.95em;
    }
    .message.sent {
        background: #4db6ac;
        color: #fff;
        margin-left: auto;
        border-bottom-right-radius: 5px;
    }
    .message.received {
        background: #f5f5f5;
        color: #333;
        margin-right: auto;
        border-bottom-left-radius: 5px;
    }
    .chat-input-container {
        padding: 15px;
        background: #fff;
        display: flex;
        align-items: center;
        border-top: 1px solid #e0e0e0;
    }
    input[name="chatInput"] {
        flex: 1;
        border: none;
        border-radius: 25px;
        padding: 12px 20px;
        background: #f1f1f1;
        box-shadow: inset 0 2px 5px rgba(0, 0, 0, 0.05);
        outline: none;
        font-size: 1em;
    }
    .send-btn {
        background: #4db6ac;
        color: #fff;
        border: none;
        border-radius: 50%;
        width: 45px;
        height: 45px;
        margin-left: 10px;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        transition: background 0.3s;
    }
    .send-btn:hover {
        background: #26a69a;
    }
    textarea[name="chatMsg"] {
        display: none;
    }
    @media (max-width: 400px) {
        .chat-container {
            width: 95%;
            height: 85vh;
        }
        .message {
            max-width: 80%;
        }
    }
</style>
<script type="text/javascript">
$(function(){
    let ws = new WebSocket("ws://${server}:${port}${path}/chatting");
    let username = "User" + Math.floor(Math.random() * 1000);
    let sessionId = null;

    ws.onopen = function() {
        $("#chatStatus").text("메시지 💨");
        console.log("WebSocket opened for user:", username);
        // 이벤트 중복 방지
        $("input[name=chatInput]").off('keydown').on("keydown", function(evt) {
            if (evt.keyCode == 13) {
                sendMessage();
            }
        });
        $(".send-btn").off('click').on("click", sendMessage);
    };

    function sendMessage() {
        let msg = $("input[name=chatInput]").val().trim();
        if (msg !== "") {
            let payload = JSON.stringify({ username: username, message: msg });
            ws.send(payload);
            console.log("Sent:", payload);
            $("input[name=chatInput]").val("");
        }
    }

  //onmessage: 서버로부터 메시지를 받으면 실행
    ws.onmessage = function(event) {
        let data = JSON.parse(event.data);
        console.log("Received:", data);
        let $msgDiv = $('<div>').addClass('message').text(data.message);
        if (data.username === username) {
            $msgDiv.addClass('sent');
        } else {
            $msgDiv.addClass('received');
        }
        $(".chat-messages").append($msgDiv);
        $(".chat-messages").scrollTop($(".chat-messages")[0].scrollHeight);
    };

  //onclose: 연결 종료 시 상태를 "connection close"로 표시.
    ws.onclose = function(event) {
        $("#chatStatus").text("info: connection closed");
        console.log("WebSocket closed:", event);
    };

    ws.onerror = function(event) {
        $("#chatStatus").text("error: connection failed");
        console.error("WebSocket error:", event);
    };
});
</script>
</head>
<body>
<div class="chat-container">
    <div id="chatStatus"></div>
    <div class="chat-messages"></div>
    <div class="chat-input-container">
        <input type="text" name="chatInput" class="w3-input" placeholder="메시지를 입력하세요...">
        <button class="send-btn">➤</button>
    </div>
</div>
<textarea name="chatMsg" rows="15" cols="40" class="w3-input" style="display:none;"></textarea>
</body>
</html>