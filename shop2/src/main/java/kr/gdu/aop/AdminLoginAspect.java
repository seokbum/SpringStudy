package kr.gdu.aop;

import jakarta.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import kr.gdu.exception.ShopException;
import kr.gdu.logic.User;

@Component
@Aspect
public class AdminLoginAspect {
	
	@Around("execution(* kr.gdu.controller.Admin*.*(..)) && args(..,session)")
	   public Object adminCheck(ProceedingJoinPoint joinPoint,
	         HttpSession session) throws Throwable{
	      User loginUser = (User)session.getAttribute("loginUser");
	      if(loginUser==null || !(loginUser instanceof User)) {
	         throw new ShopException("[AdminCheck]로그인부터하세요", "../user/login");         
	      }
	      
	      String userId = loginUser.getUserid();
	      
	      if( !(userId.equals("admin"))) {
	         throw new ShopException("[AdminCheck]관리자만 접근가능", "../user/mypage?userid="+userId);    
	      }
	      return joinPoint.proceed();
	}
}
