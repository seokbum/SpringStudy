package kr.gdu.aop;




import org.aspectj .lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSession;
import kr.gdu.exception.ShopException;
import kr.gdu.logic.Cart;
import kr.gdu.logic.User;



@Component
@Aspect
public class CartAspect {
	/*
	 * poincut : Cartcontroller클래스의 매개변수의 마지막이 HttpSession이고
	 * check* 로 시작하는 메서드
	 * 
	 * advice : Before로 설정
	 */
	
	@Before("execution(* kr.gdu.controller.Cart*.check*(..)) && args(..,session)")
	public void userIdCheck(HttpSession session) throws Throwable{
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser==null) {
			throw new ShopException("회원만 주문이가능합니다", "../user/login");
		}
		Cart cart=(Cart)session.getAttribute("CART");
		if(cart==null || cart.getItemSetList().size()==0) {
			throw new ShopException("장바구니에 상품을 추가하세요", "../item/list");
		}
	}

}
