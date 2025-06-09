package controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Cart;
import logic.Item;
import logic.ItemSet;
import service.ShopService;

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
		List<ItemSet> list = cart.getItemSetList();
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getItem().equals(item)) {
				quantity += list.get(i).getQuantity();
				list.remove(i);
			}
			
		}
		cart.push(new ItemSet(item, quantity));
		mav.addObject("message",item.getName()+":"+quantity+"개 장바구니 추가");
		mav.addObject("cart",cart);
		return mav;
		
	}
	
}
