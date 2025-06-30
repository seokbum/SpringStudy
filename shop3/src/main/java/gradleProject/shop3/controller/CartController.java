package gradleProject.shop3.controller;

import gradleProject.shop3.domain.Cart;
import gradleProject.shop3.domain.Item;
import gradleProject.shop3.domain.ItemSet;
import gradleProject.shop3.dto.ItemAddDto;
import gradleProject.shop3.service.ShopService;
import gradleProject.shop3.util.CipherUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
// (필요에 따라 User, Sale, Map, HashMap, ResponseBody 등을 import)
import gradleProject.shop3.domain.User;
import gradleProject.shop3.domain.Sale;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("cart")
public class CartController {

    @Autowired
    private ShopService service;

    @PostMapping("cartAdd")
    public String add(ItemAddDto dto, HttpSession session, Model model) {
        int id = dto.getId();
        int quantity = dto.getQuantity();

        Item item = service.getItem(id);
        if (item == null) {
            model.addAttribute("message", "상품 정보를 찾을 수 없습니다.");
            model.addAttribute("cart", session.getAttribute("CART"));
            return "cart/cart";
        }

        Cart cart = (Cart) session.getAttribute("CART");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("CART", cart);
        }

        // --- 핵심 수정 로직 ---
        boolean itemFound = false;
        for (ItemSet is : cart.getItemSetList()) {
            if (is.getItem().getId() == id) {
                is.setQuantity(is.getQuantity() + quantity);
                itemFound = true;
                model.addAttribute("message", item.getName() + "의 수량을 " + quantity + "개 추가했습니다.");
                break;
            }
        }

        if (!itemFound) {
            cart.push(new ItemSet(item, quantity));
            model.addAttribute("message", item.getName() + ":" + quantity + "개 장바구니 추가");
        }
        // --- 핵심 수정 로직 종료 ---

        model.addAttribute("cart", cart);
        System.out.println(cart);
        return "cart/cart";
    }


    @GetMapping("cartDelete")
    public ModelAndView cartDelete(int index, HttpSession session) {
        ModelAndView mav = new ModelAndView("cart/cart");
        Cart cart = (Cart) session.getAttribute("CART");

        if (cart != null && cart.getItemSetList().size() > index) {
            ItemSet removeObj = cart.getItemSetList().remove(index);
            mav.addObject("message", removeObj.getItem().getName() + "이(가) 삭제되었습니다");
        } else {
            mav.addObject("message", "삭제할 상품을 찾지 못했습니다.");
        }

        mav.addObject("cart", cart);
        return mav;
    }

    @RequestMapping("cartView")
    public String view(HttpSession session, Model model) {
        model.addAttribute("message", "장바구니 상품 조회");
        Cart cart = (Cart) session.getAttribute("CART");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("CART", cart);
        }
        model.addAttribute("cart", cart);
        return "cart/cart";
    }

    @RequestMapping("checkout")
    public String checkout(Model model, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        Cart cart = (Cart) session.getAttribute("CART");

        try {
            String key = CipherUtil.makehash(loginUser.getUserid());
            String plainEmail = CipherUtil.decrypt(loginUser.getEmail(), key);
            loginUser.setEmail(plainEmail);
        } catch (Exception e) {
            loginUser.setEmail("(이메일 확인 불가)");
        }

        model.addAttribute("loginUser", loginUser);
        model.addAttribute("cart", cart);

        return "cart/checkout";
    }

    // check로시작하며 session매개변수 -> AOP사용
    // 주문확정 클릭시 동작하는 컨트롤러
    @GetMapping("end")
    public ModelAndView checkEnd(HttpSession session) {
        ModelAndView mav = new ModelAndView();
        Cart cart = (Cart) session.getAttribute("CART");// 장바구니상품(List<ItemSet>)
        User loginUser = (User) session.getAttribute("loginUser");// User
        Sale sale = service.checkEnd(loginUser, cart);
        session.removeAttribute("CART");// 장바구니 초기화
        mav.addObject("sale", sale);
        return mav;

    }
    //
    // @RequestMapping("kakao")
    // @ResponseBody //view없이 바로 데이터를 클라이언트로전송
    // public Map<String, Object> kakao(HttpSession session) {
    //    HashMap<String, Object> map = new HashMap<>();
    //    Cart cart = (Cart) session.getAttribute("CART");
    //    User loginUser = (User) session.getAttribute("loginUser");
    //    map.put("merchant_uid", loginUser.getUserid() + "-" + session.getId());
    //    map.put("name", cart.getItemSetList().get(0).getItem().getName() + "외" + (cart.getItemSetList().size() - 1));
    //    map.put("amount", cart.getTotal());
    //    map.put("buyer_email", loginUser.getEmail());
    //    map.put("buyer_name", loginUser.getUsername());
    //    map.put("buyer_tel", loginUser.getPhoneno());
    //    map.put("buyer_addr", loginUser.getAddress());
    //    map.put("buyer_postcode", loginUser.getPostcode());
    //    System.out.println(map);
    //    return map;
    //
    // }
}