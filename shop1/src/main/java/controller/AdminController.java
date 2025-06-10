package controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.User;
import service.UserService;

@Controller
@RequestMapping("admin")
public class AdminController {
	@Autowired
	private UserService service;
	
	@GetMapping("list")
	public ModelAndView callList(HttpSession session) {
		ModelAndView mav = new ModelAndView("admin/list");
		List<User> userlist = service.list();
		mav.addObject("list",userlist);
		return mav;
	}
}
