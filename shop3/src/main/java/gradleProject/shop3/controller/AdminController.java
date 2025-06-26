package gradleProject.shop3.controller;

import gradleProject.shop3.domain.Mail;
import gradleProject.shop3.domain.User;
import gradleProject.shop3.exception.ShopException;
import gradleProject.shop3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    UserService service;

    @GetMapping("/list")
    public String callList(Model model, HttpSession session) {
        // ★★★ 이메일 복호화 기능이 포함된 서비스 메소드 호출 ★★★
        List<User> list = service.findAllAndDecrypt();
        model.addAttribute("list", list);
        return "admin/list";
    }

    // ★★★ 메일 기능 활성화 ★★★
    @GetMapping("mail")
    public String mailForm(String[] idchks, Model model) {
        if (idchks == null || idchks.length == 0) {
            throw new ShopException("메일을 보낼 대상자를 선택하세요", "list");
        }
        // ID로 사용자를 찾고 이메일도 복호화하는 서비스 메소드 호출
        List<User> list = service.findUsersByIdsAndDecrypt(idchks);

        Mail mail = new Mail();
        StringBuilder sb = new StringBuilder();
        for (User u : list) {
            sb.append(u.getUsername()).append("<").append(u.getEmail()).append(">,");
        }
        mail.setRecipient(sb.toString());
        model.addAttribute("mail", mail);
        model.addAttribute("list", list); // 받는 사람 목록을 화면에 보여주기 위해 추가
        return "admin/mail";
    }

    @PostMapping("mail")
    public ModelAndView mailSend(@Valid Mail mail, BindingResult bresult) {
        ModelAndView mav = new ModelAndView("alert");
        if (bresult.hasErrors()) {
            mav.addObject("message", "메일 전송 실패: 모든 필수 항목을 입력하세요.");
            mav.addObject("url", "list");
            return mav;
        }

        if (service.mailSend(mail)) {
            mav.addObject("message", "메일 전송이 완료되었습니다.");
        } else {
            mav.addObject("message", "메일 전송을 실패했습니다.");
        }
        mav.addObject("url", "list");
        service.mailfileDelete(mail);
        return mav;
    }
}