package kr.gdu.controller;

import kr.gdu.dto.JoinDto;
import kr.gdu.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

	@Autowired
	LoginService loginService;

	@GetMapping("/") //http://localhost:8080
	public String root(Model model) {
		model.addAttribute("msg","/로 요청");
		return "home";
	}
	@GetMapping("/home")
	public String home(Model model) {
		model.addAttribute("msg","/home로 요청");
		return "home";
	}
	@GetMapping("/my")
	public String my(Model model) {
		model.addAttribute("msg","/my로 요청");
		return "home";
	}
	@GetMapping("/admin")
	public String admin(Model model) {
		model.addAttribute("msg","/admin로 요청");
		return "home";
	}
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/join")
	public String join() {
		return "join";
	}

	@PostMapping("/joinProc")
	public String joinProc(JoinDto joinDto) {
		System.out.println(joinDto.getUsername());
		loginService.joinProcess(joinDto);
		return "redirect:/login";
	}
}
