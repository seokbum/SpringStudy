package kr.gdu.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.gdu.exception.ShopException;
import kr.gdu.logic.Mail;
import kr.gdu.logic.User;
import kr.gdu.service.UserService;


@Controller
@RequestMapping("admin")
public class AdminController {
	
	@Autowired
	UserService service;
	
	@GetMapping("/list")
	public String callList(Model model,HttpSession session) {
		List<User> list = service.selectList(); //모든 User객체를 꺼내옴
		model.addAttribute("list",list);
		return "/admin/list";
	}
	
	@GetMapping("mail")
	public String mail(String[] idchks ,HttpSession session , Model model ) {
		if(idchks==null || idchks.length==0) {
			throw new ShopException("메일을 보낼 대상자를 선택하세요","list");
		}
		//idchks파라미터에 속한 userid에 해당하는 User객체들
		List<User> list = service.getUserList(idchks);
		model.addAttribute("list",list);
		Mail mail = new Mail();
		StringBuilder sb = new StringBuilder();
		for(User u : list) {
			sb.append(u.getUsername()).append("<").append(u.getEmail())
			.append(">").append(",");
		}
		mail.setRecipient(sb.toString());
		model.addAttribute("mail",mail);
		return "admin/mail";
	}
	
	@PostMapping("mail")
	public String mail(@Valid @ModelAttribute Mail mail, BindingResult bresult,
			Model model) {
		if(bresult.hasErrors()) {
			return "admin/mail";
		}
		if(service.mailSend(mail)) {
			model.addAttribute("message","메일전송이완료되었습니다");
		}
		else {
			model.addAttribute("message","메일전송을 실패했습니다");
		}
		model.addAttribute("url","list");
		//첨부파일제거하기
		service.mailfileDelete(mail);
		return "alert";
	}
}
