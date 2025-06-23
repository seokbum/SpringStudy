package kr.gdu.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.gdu.logic.Item;
import kr.gdu.service.ShopService;

@Controller
@RequestMapping("item") // http://localhost:8080/shop1/item
public class ItemController {
	
	@Autowired
	private ShopService service;
	
	//http://localhost:8080/shop1/item/list 요청 시 호출되는메서드
	@RequestMapping("list")
	public String list(Model model) {
		//ModelAndView : view에 이름과 , 전달데이터를 저장
		ModelAndView mav = new ModelAndView();
		List<Item> itemList = service.itemList();
		model.addAttribute("itemList",itemList);
		return "item/list";
	}
	@GetMapping({"detail","update","delete"})
	public ModelAndView getMap(@RequestParam Integer id) {
		ModelAndView mav = new ModelAndView();
		Item item = service.getItem(id);
		mav.addObject("item",item);
		return mav;
	}
	
	@GetMapping("create") //GET방식
	public ModelAndView create() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new Item());
		return mav;
	}
	
	@PostMapping("create")
	//valid : 유효성검사
	//BindingResult : error를 담고있는 객체
	public ModelAndView register(@Valid Item item, BindingResult bresult,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) { //입력값 검증 오류발생 시 
			return mav;
		}
		//정상인경우
		service.itemCreate(item,request);//DB등록 + 이미지파일업로드
		mav.setViewName("redirect:list"); //list 재요청
		return mav;
	}
	
	@PostMapping("update")
	public ModelAndView postUpdate(@Valid Item item, BindingResult bresult,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			return mav;
		}
		service.itemUpdate(item,request);
		mav.setViewName("redirect:list"); //list 재요청
		return mav;
	}
	
	@PostMapping("delete")
	public ModelAndView postDelete(@RequestParam Integer id) {
		ModelAndView mav = new ModelAndView();
		service.deleteItem(id);
		mav.setViewName("redirect:list"); //list 재요청
		return mav;
	}
	
	
	/*
	//detail,update는 get방식일때 똑같은 메서드를 사용함
	@GetMapping("detail")
	public ModelAndView detail(@RequestParam Integer id) {
		ModelAndView mav = new ModelAndView();
		Item item = service.getItem(id);
		mav.addObject("item",item);
		return mav;
	}
	
	@GetMapping("create") //GET방식
	public ModelAndView create() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new Item());
		return mav;
	}
	@PostMapping("create")
	//valid : 유효성검사
	//BindingResult : error를 담고있는 객체
	public ModelAndView register(@Valid Item item, BindingResult bresult,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) { //입력값 검증 오류발생 시 
			return mav;
		}
		//정상인경우
		service.itemCreate(item,request);//DB등록 + 이미지파일업로드
		mav.setViewName("redirect:list"); //list 재요청
		return mav;
	}
	
	@GetMapping("update")
	public ModelAndView getUpdate(@RequestParam Integer id) {
		ModelAndView mav = new ModelAndView();
		Item item = service.getItem(id);
		mav.addObject("item",item);
		return mav;
	}
	
	@PostMapping("update")
	public ModelAndView postUpdate(@Valid Item item, BindingResult bresult,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			return mav;
		}
		service.itemUpdate(item,request);
		mav.setViewName("redirect:list"); //list 재요청
		return mav;
	}
	
	
	@GetMapping("delete")
	public ModelAndView getDelete(@RequestParam Integer id) {
		ModelAndView mav = new ModelAndView();
		Item item = service.getItem(id);
		mav.addObject("item",item);
		return mav;
	}
	
	
	*/

}
