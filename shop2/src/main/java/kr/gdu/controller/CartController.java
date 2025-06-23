package kr.gdu.controller;

import java.util.HashMap;

import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import jakarta.servlet.http.HttpSession;
import kr.gdu.logic.Cart;
import kr.gdu.logic.Item;
import kr.gdu.logic.ItemSet;
import kr.gdu.logic.Sale;
import kr.gdu.logic.User;
import kr.gdu.service.ShopService;


@Controller
@RequestMapping("cart")
public class CartController {

	@Autowired
	private ShopService service;

	
	@RequestMapping("cartAdd")
	public ModelAndView add(Integer id , Integer quantity,HttpSession session) {
		//new ModelAndView(뷰명) : /WEB-INF/view/cart/cart.jsp
		ModelAndView mav = new ModelAndView("cart/cart");
		Item item = service.getItem(id);
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart==null) {
			cart = new Cart();
			session.setAttribute("CART", cart);
		}
		cart.push(new ItemSet(item, quantity));
		
		mav.addObject("message",item.getName()+":"+quantity+"개 장바구니 추가");
		mav.addObject("cart",cart);
		System.out.println(cart);
		return mav;
	}
	
	@GetMapping("cartDelete")
	public ModelAndView cartDelete(int index , HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Cart cart = (Cart)session.getAttribute("CART");
		/*
		 * E remove(int index) : 인덱스에해당하는 객체 제거 후 (제거된 객체 return)
		 * boolean remove(Object) : 객체를 입력받아 해당객체 제거 후 (제거 여부 반환)
		 */
		ItemSet removeObj = cart.getItemSetList().remove(index);
		mav.addObject("message",removeObj.getItem().getName()+"이(가) 삭제되었습니다");
		mav.addObject("cart",cart);
		return mav;
	}
	
	@RequestMapping("cartView")
	public ModelAndView view(HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		mav.addObject("message","장바구니 상품 조회");
		mav.addObject("cart",session.getAttribute("CART"));
		return mav;
	}
	
	/*
	 * 주문전 확인 페이지
	 * 1.장바구니에 상품존재해야함
	 * 		상품이없는경우 예외발생
	 *2.로그인 된 상태여야함
	 *로그아웃상태 -> 예외발생
	 */
	@RequestMapping("checkout")
	public String checkout(HttpSession session) {
		return null;
	}
	
	//check로시작하며 session매개변수 -> AOP사용
	//주문확정 클릭시 동작하는 컨트롤러
	@GetMapping("end")
	public ModelAndView checkEnd(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		Cart cart = (Cart)session.getAttribute("CART");//장바구니상품(List<ItemSet>)
		User loginUser = (User)session.getAttribute("loginUser");//User
		Sale sale = service.checkend(loginUser,cart);
		session.removeAttribute("CART");//장바구니 초기화
		mav.addObject("sale",sale);
		return mav;
		
	}
	@RequestMapping("kakao")
	@ResponseBody // 뷰없이 바로 데이터를 클라이언트로 전송
	public Map<String,Object> kakao(HttpSession session) {
		Map<String, Object> map = new HashMap<>();
		Cart cart = (Cart)session.getAttribute("CART");
		User loginUser = (User)session.getAttribute("loginUser");
		// merchant_uid : 주문의 고유 아이디
		map.put("merchant_uid", loginUser.getUserid()+"-"+session.getId());
		map.put("name", cart.getItemSetList().get(0).getItem().getName()+ "외 "+ (cart.getItemSetList().size()-1));
		map.put("amount",cart.getTotal()); // 전체 주문 금액
		map.put("buyer_email", loginUser.getEmail()); // 구매자 이메일
		map.put("buyer_name", loginUser.getUsername()); // 구매자 이름 
		map.put("buyer_tel", loginUser.getPhoneno()); // 구매자 번호
		map.put("buyer_addr", loginUser.getAddress()); // 구매자 주소
		map.put("buyer_postcode", loginUser.getPostcode()); // 구매자 우편번호
		return map;
	}
}
