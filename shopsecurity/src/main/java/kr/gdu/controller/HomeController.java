package kr.gdu.controller;

import kr.gdu.dto.JoinDto;
import kr.gdu.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collection;
import java.util.Iterator;

@Controller
public class HomeController {

    @Autowired
    LoginService loginService;

    @GetMapping("/") //http://localhost:8080
    public String root(Model model) {
        model.addAttribute("msg", "/로 요청");
        return "home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("msg", "/home로 요청");
        return "home";
    }

    @GetMapping("/my")
    public String my(Model model) {
        /*
            로그인한 사용자 정보와 권한 조회하기
         */
        // 로그인된 아이디 조회
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        // 인증 정보 조회 : 사용자정보, 권한 정보
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        model.addAttribute("msg", "/my로 접근 : id=" +id +", role=" +role);
        return "home";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("msg", "/admin로 요청");
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
