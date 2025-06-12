package kr.gdu.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.gdu.logic.User;
import kr.gdu.service.UserService;

@Controller
@RequestMapping("admin")
public class AdminController {
	@Autowired
	private UserService service;
	
	@GetMapping("list")
	public ModelAndView List(HttpSession session) {
		ModelAndView mav = new ModelAndView("admin/list");
		List<User> list = service.list();
		mav.addObject("list",list);
		return mav;
	}
}
