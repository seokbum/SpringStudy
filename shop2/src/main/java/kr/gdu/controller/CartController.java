package kr.gdu.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.gdu.logic.Cart;
import kr.gdu.logic.Item;
import kr.gdu.logic.ItemSet;
import kr.gdu.logic.Sale;
import kr.gdu.logic.User;
import kr.gdu.service.ShopService;

@Controller
@RequestMapping("cart")
public class CartController {

    private final ItemController itemController;
	/*
	 * 1. 문제 
	 * 장바구니에 존재하는 상품의 경우 수량만 증가하기
	 * 장바구니에 존재하는 상품이 아닌 경우 상품 추가하기
	 * 
	 * 2. 문제
	 * 비밀번호 찾기를 비밀번호 초기화로 수정하기
	 * 기존 비밀번호:1234 
	 * 비밀번호 초기화 : 전체 6자리의 대문자/소문자/숫자 임의의 조합으로 변경하기
	 * 				   사용자에게 출력하기
	 */
	@Autowired
	private ShopService service;

    CartController(ItemController itemController) {
        this.itemController = itemController;
    }
	
	@RequestMapping("cartAdd")
	public ModelAndView add(Integer id, Integer quantity, HttpSession session) {
		
		//new ModelAndView(뷰명) : /WEB-INF/view/cart/cart.jsp 
		ModelAndView mav = new ModelAndView("cart/cart");
		Item item = service.getItem(id); // id의 해당하는 item 객체
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart == null) {//session 의 CART 이름의 객체가 없는 경우
			cart = new Cart();
			session.setAttribute("CART", cart);
		}
			
		cart.push(new ItemSet(item, quantity));
		mav.addObject("message",item.getName()+":"+quantity+"개 장바구니 추가");
		mav.addObject("cart",cart);
		return mav;
	}
	
	@RequestMapping("cartDelete")
	public ModelAndView delete(int index,HttpSession session) {
		
		ModelAndView mav = new ModelAndView("cart/cart");//jsp 주소
		
		Cart cart = (Cart)session.getAttribute("CART");
		
		ItemSet removeObj = cart.getItemSetList().remove(index); // 삭제 후 객체 전달
		/*
		 	E remove(int) : 인덱스에 해당하는 객체를 제거. 제거된 객체를 리턴
		 	boolean remove(Object) : 객체를 입력받아서 객체를 제거. 제거 여부를 리턴
		 */
		
		mav.addObject("message",removeObj.getItem().getName()
				+ "가(이) 삭제 되었습니다.");
		mav.addObject("cart",cart);
		return mav;
	}
	
	@RequestMapping("cartView")
	public ModelAndView view(HttpSession session) {
		
		ModelAndView mav = new ModelAndView("cart/cart");
		mav.addObject("message", "장바구니 상품 조회");
		mav.addObject("cart",session.getAttribute("CART"));
		return mav;
	}
	
	/*
	 * 주문전 확인 페이지
	 * 1. 장바구니에 상품 존재해야함
	 * 	  상품이 없는 경우 예외 발생.
	 * 2. 로그인 된 상태여야함
	 * 	  로그아웃 상태 : 예외 발생
	 */
	@RequestMapping("checkout")
	public String checkout(HttpSession session) {
		return null;
	}
	
	@RequestMapping("end")
	public ModelAndView checkend(HttpSession session) {
		
		ModelAndView mav = new ModelAndView();
		
		Cart cart = (Cart)session.getAttribute("CART"); // 장바구니 상품
		User loginUser = (User)session.getAttribute("loginUser"); // 로그인 정보
		Sale sale = service.checkend(loginUser,cart);
		
		session.removeAttribute("CART"); // 장바구니 제거
		mav.addObject("sale",sale);
		return mav;
	}
	
}
