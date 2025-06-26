package gradleProject.shop3.controller;

import gradleProject.shop3.exception.ShopException; // ShopException 클래스 임포트
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ShopException.class)
    public String handleShopException(ShopException e, Model model) {
        model.addAttribute("message", e.getMessage());
        model.addAttribute("redirectUrl", e.getUrl());
        model.addAttribute("title", "오류 발생"); // 에러 페이지의 타이틀 설정 (선택 사항)

        // 파일이 'templates/error/error.html'에 있으므로, 'error/error'를 반환합니다.
        return "error/error";
    }
}