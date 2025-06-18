<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
    <!-- isErrorPage="true" : ShopException객체가 전달 -->
<script type="text/javascript">
/*
 isErrorPage=:true설정으로인해 
 암묵적으로 객체이름을 exception으로 사용가능하게해줌 
 get프로퍼티로 인해 다음과같이 message,url을 사용가능하게 해
 */
alert("${exception.message}")
location.href="${exception.url}"
</script>