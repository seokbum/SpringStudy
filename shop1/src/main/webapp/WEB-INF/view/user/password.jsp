<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>비밀번호 변경</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
</head>
<body>

<div class="container my-5">
    <h2 class="mb-4 text-center"><i class="bi bi-key-fill me-2"></i>비밀번호 변경</h2>

    <div class="row justify-content-center">
        <div class="col-md-6 col-lg-5">
            <div class="card shadow-sm p-4">
                <form action="password" method="post" name="f" onsubmit="return inchk(this)">
                    <div class="mb-3">
                        <label for="password" class="form-label">현재 비밀번호</label>
                        <input type="password" class="form-control" id="password" name="password" placeholder="현재 비밀번호를 입력하세요">
                        <div class="invalid-feedback" id="passwordFeedback"></div>
                    </div>
                    <div class="mb-3">
                        <label for="chgpass" class="form-label">변경 비밀번호</label>
                        <input type="password" class="form-control" id="chgpass" name="chgpass" placeholder="새 비밀번호를 입력하세요">
                        <div class="invalid-feedback" id="chgpassFeedback"></div>
                    </div>
                    <div class="mb-3">
                        <label for="chgpass2" class="form-label">변경 비밀번호 재입력</label>
                        <input type="password" class="form-control" id="chgpass2" name="chgpass2" placeholder="새 비밀번호를 다시 입력하세요">
                        <div class="invalid-feedback" id="chgpass2Feedback"></div>
                    </div>
                    <div class="d-grid mt-4">
                        <button type="submit" class="btn btn-primary btn-lg">
                            <i class="bi bi-arrow-clockwise me-2"></i>비밀번호 변경
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
	function inchk(f) {
        // 기존 피드백 메시지 초기화
        document.querySelectorAll('.is-invalid').forEach(el => el.classList.remove('is-invalid'));
        document.querySelectorAll('.invalid-feedback').forEach(el => el.textContent = '');

		if(f.password.value === "") {
			f.password.classList.add('is-invalid');
			document.getElementById('passwordFeedback').textContent = "현재 비밀번호를 입력하세요.";
			f.password.focus();
			return false;
		}
		if(f.chgpass.value === "") {
			f.chgpass.classList.add('is-invalid');
			document.getElementById('chgpassFeedback').textContent = "변경 비밀번호를 입력하세요.";
			f.chgpass.focus();
			return false;
		}
		if(f.chgpass.value !== f.chgpass2.value){
			f.chgpass2.classList.add('is-invalid');
			document.getElementById('chgpass2Feedback').textContent = "변경 비밀번호와 재입력이 다릅니다.";
			f.chgpass2.value = "";
			f.chgpass2.focus();
			return false;
		}
		return true;
	}
</script>
</body>
</html>