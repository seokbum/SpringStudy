package gradleProject.shop3.controller;

import gradleProject.shop3.domain.Cart;
import gradleProject.shop3.domain.Item;
import gradleProject.shop3.domain.ItemSet;
import gradleProject.shop3.dto.ItemAddDto;
import gradleProject.shop3.service.ShopService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("cart")
public class CartController {

	@Autowired
	private ShopService service;

	@PostMapping("cartAdd")
	//id와 quantity파라미터를 따로 가져오기보다는 modelAttribute를 활용해 dto객체에 담아서가져오자
	public String add(ItemAddDto dto, HttpSession session, Model model) {
		int id = dto.getId();
		int quantity = dto.getQuantity();
		Item item = service.getItem(id);
		Cart cart = (Cart) session.getAttribute("CART");
		if (cart == null) {
			cart = new Cart();
			session.setAttribute("CART", cart);
		}
		cart.push(new ItemSet(item, quantity));
		model.addAttribute("message", item.getName() + ":" + quantity + "개 장바구니 추가");
		model.addAttribute("cart", cart);
		System.out.println(cart);
		return "cart/cart"; // /WEB-INF/views/cart/cart.jsp뷰로이동
	}


	@GetMapping("cartDelete")
	public ModelAndView cartDelete(int index, HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Cart cart = (Cart) session.getAttribute("CART");
		/*
		 * E remove(int index) : 인덱스에해당하는 객체 제거 후 (제거된 객체 return) boolean remove(Object)
		 * : 객체를 입력받아 해당객체 제거 후 (제거 여부 반환)
		 */
		ItemSet removeObj = cart.getItemSetList().remove(index);
		mav.addObject("message", removeObj.getItem().getName() + "이(가) 삭제되었습니다");
		mav.addObject("cart", cart);
		return mav;
	}

	@RequestMapping("cartView")
	public String view(HttpSession session,Model model) {
		model.addAttribute("message", "장바구니 상품 조회");
		Cart cart = (Cart) session.getAttribute("CART");
		if (cart == null) {
			cart = new Cart();
			session.setAttribute("CART", cart);
		}
		model.addAttribute("cart", session.getAttribute("CART"));
		return "cart/cart";
	}

	/*
	 * 주문전 확인 페이지 1.장바구니에 상품존재해야함 상품이없는경우 예외발생 2.로그인 된 상태여야함 로그아웃상태 -> 예외발생
	 */
	@RequestMapping("checkout")
	public String checkout(HttpSession session) {
		return null;
	}

	// check로시작하며 session매개변수 -> AOP사용
	// 주문확정 클릭시 동작하는 컨트롤러
//	@GetMapping("end")
//	public ModelAndView checkEnd(HttpSession session) {
//		ModelAndView mav = new ModelAndView();
//		Cart cart = (Cart) session.getAttribute("CART");// 장바구니상품(List<ItemSet>)
//		User loginUser = (User) session.getAttribute("loginUser");// User
//		Sale sale = service.checkend(loginUser, cart);
//		session.removeAttribute("CART");// 장바구니 초기화
//		mav.addObject("sale", sale);
//		return mav;
//	}
//
//	@RequestMapping("kakao")
//	@ResponseBody //view없이 바로 데이터를 클라이언트로전송
//	public Map<String, Object> kakao(HttpSession session) {
//		HashMap<String, Object> map = new HashMap<>();
//		Cart cart = (Cart) session.getAttribute("CART");
//		User loginUser = (User) session.getAttribute("loginUser");
//		map.put("merchant_uid", loginUser.getUserid() + "-" + session.getId());
//		map.put("name", cart.getItemSetList().get(0).getItem().getName() + "외" + (cart.getItemSetList().size() - 1));
//		map.put("amount", cart.getTotal());
//		map.put("buyer_email", loginUser.getEmail());
//		map.put("buyer_name", loginUser.getUsername());
//		map.put("buyer_tel", loginUser.getPhoneno());
//		map.put("buyer_addr", loginUser.getAddress());
//		map.put("buyer_postcode", loginUser.getPostcode());
//		System.out.println(map);
//		return map;
//
//	}
}