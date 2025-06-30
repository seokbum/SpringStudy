package gradleProject.shop3.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("chat")
public class ChatController {
	
	@RequestMapping("*")
	public String dumy() {
		return null;
	}

	@RequestMapping("chat")
	public String chat(Model model, HttpServletRequest request) {
		model.addAttribute("port", request.getLocalPort());
		model.addAttribute("server", request.getServerName());
		model.addAttribute("path", request.getContextPath());
		return "chat/chat";
	}
}
