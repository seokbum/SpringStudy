package kr.gdu.aop;




import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSession;
import kr.gdu.exception.ShopException;
import kr.gdu.logic.User;



/*
 *  1. poincut : UserController.idCheck* 메서드로시작
 *  		마지막 매개변수가 String,HtppSession인 메서드
 */
@Component
@Aspect
public class UserLoginAspect {
	//UserController에 idCheck로 시작하는 모든 메서드를 실행하기 전,후(around)에 해당메서드호출
	@Around("execution(* kr.gdu.controller.User*.idCheck*(..)) && args(..,userid,session)")
	public Object userIdCheck(ProceedingJoinPoint joinPoint,
			String userid,HttpSession session) throws Throwable{
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser==null) {
			throw new ShopException("[idCheck]로그인 필요", "login");			
		}
		if(!loginUser.getUserid().equals("admin")
				&& !loginUser.getUserid().equals(userid)) {
			throw new ShopException("[idCheck]본인 정보만 거래가능", "../item/list");
		}
		return joinPoint.proceed();
	}
	
	@Around("execution(* kr.gdu.controller.User*.loginCheck*(..)) && args(..,session)")
	public Object loginCheck(ProceedingJoinPoint joinPoint,
			HttpSession session) throws Throwable{
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser==null || !(loginUser instanceof User)) {
			throw new ShopException("[loginChk]로그인 필요", "login");			
		}
		return joinPoint.proceed();
	}
	
	@Around("execution(* kr.gdu.controller.Admin*.call*(..)) && args(..,session)")
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
