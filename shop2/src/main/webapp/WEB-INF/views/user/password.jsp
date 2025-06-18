<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호변경</title>
</head>
<body>
<h2>비밀번호변경</h2>
<form action="password" method="post" name="f" onsubmit="return inchk(this)">
<table>
<tr><th width="20%">현재비밀번호</th>
	<td><input type="password" name="password"></td></tr>
<tr><th>변경비밀번호</th><td><input type="password" name="chgpass"></td></tr>
<tr><th>변경비밀번호 재입력</th><td><input type="password" name="chgpass2"></td></tr>
<tr><td colspan="2"><input type="submit" value="비밀번호변경"></td></tr>
</table>
</form>
<script type="text/javascript">
function inchk(f){

	if(f.password.value==""){
		alert("현재비밀번호입력바람");
		f.password.focus();
		return false;
	}
	if(f.chgpass.value != f.chgpass2.value){
		alert("변경비밀번호와 재입력한비밀번호가 달라요");
		f.chgpass2.value="";
		f.chgpass2.focus();
		return false
	}
	return true;
}
</script>
</body>
</html>