package gradleProject.shop3.controller;

import gradleProject.shop3.domain.Sale;
import gradleProject.shop3.domain.User;
import gradleProject.shop3.dto.UserDto;
import gradleProject.shop3.exception.ShopException;
import gradleProject.shop3.mapper.UserMapper;
import gradleProject.shop3.service.ShopService;
import gradleProject.shop3.service.UserService;
import gradleProject.shop3.util.CipherUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.List;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ShopService shopService;


    @GetMapping("*")
    public String form(Model model, HttpSession session, HttpServletRequest request) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser != null && StringUtils.hasText(loginUser.getUserid())) {
            return "redirect:/user/mypage?userid=" + loginUser.getUserid();
        }
        String requestURI = request.getRequestURI();
        if (requestURI.endsWith("/user/join")) {
            model.addAttribute("user", new User());
            model.addAttribute("title", "회원가입");
            return "user/join";
        } else if (requestURI.endsWith("/user/login")) {
            model.addAttribute("user", new User());
            model.addAttribute("title", "로그인");
            return "user/login";
        } else {
            model.addAttribute("user", new User());
            model.addAttribute("title", "회원가입");
            return "user/join";
        }
    }

    @GetMapping("idSearch")
    public String idSearchForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("title", "아이디 찾기");
        return "user/idSearch";
    }

    @GetMapping("pwSearch")
    public String pwSearchForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("title", "비밀번호 찾기");
        return "user/pwSearch";
    }

    @PostMapping("idSearch")
    public String idSearch(@ModelAttribute User user, Model model) {
        model.addAttribute("title", "아이디 찾기");
        if (!StringUtils.hasText(user.getEmail()) || !StringUtils.hasText(user.getPhoneno())) {
            model.addAttribute("result", "이메일과 전화번호를 모두 입력하세요.");
            model.addAttribute("user", user);
            return "user/idSearch";
        }
        String resultId = service.findUserId(user.getEmail(), user.getPhoneno());
        if (resultId == null) {
            model.addAttribute("result", "입력하신 정보와 일치하는 사용자가 없습니다.");
        } else {
            model.addAttribute("result", "회원님의 아이디는 [ " + resultId + " ] 입니다.");
        }
        model.addAttribute("user", user);
        return "user/idSearch";
    }

    @PostMapping("pwSearch")
    public String pwSearch(@ModelAttribute User user, Model model) {
        model.addAttribute("title", "비밀번호 찾기");
        if (!StringUtils.hasText(user.getUserid()) || !StringUtils.hasText(user.getEmail()) || !StringUtils.hasText(user.getPhoneno())) {
            model.addAttribute("result", "아이디, 이메일, 전화번호를 모두 입력하세요.");
            model.addAttribute("user", user);
            return "user/pwSearch";
        }
        // ★★★ 이 부분만 수정 ★★★
        boolean userExists = service.verifyUserForPasswordReset(user.getUserid(), user.getEmail(), user.getPhoneno());
        if (userExists) {
            String newPassword = generateRandomPassword();
            service.updatePassword(user.getUserid(), CipherUtil.makehash(newPassword));
            model.addAttribute("result", "임시 비밀번호가 발급되었습니다: [ " + newPassword + " ] 로그인 후 바로 변경해주세요.");
        } else {
            model.addAttribute("result", "입력하신 정보와 일치하는 사용자가 없습니다.");
        }
        model.addAttribute("user", user);
        return "user/pwSearch";
    }

    private String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    @PostMapping("join")
    public String userAdd(@Valid UserDto userDto, BindingResult bresult, Model model) {
        model.addAttribute("title", "회원가입");
        if (bresult.hasErrors()) {
            bresult.reject("error.input.user", "입력 값을 확인해 주세요.");
            return "user/join";
        }
        try {
            String cipherPass = CipherUtil.makehash(userDto.getPassword());
            userDto.setPassword(cipherPass);
            String cipherUserid = CipherUtil.makehash(userDto.getUserid());
            String cipherEmail = CipherUtil.encrypt(userDto.getEmail(), cipherUserid);
            userDto.setEmail(cipherEmail);
            User user = userMapper.toEntity(userDto);
            service.userInsert(user);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bresult.reject("error.duplicate.user", "이미 존재하는 아이디입니다.");
            return "user/join";
        } catch (Exception e) {
            e.printStackTrace();
            bresult.reject("error.join.failed", "회원가입 중 알 수 없는 오류가 발생했습니다.");
            return "user/join";
        }
        return "redirect:/user/login";
    }

    @GetMapping("login")
    public String callLogin(HttpSession session, Model model) {
        String clientId = "7QpF5ZIsnvhvX9aZqwYN";
        String redirectURL = null;
        try {
            redirectURL = URLEncoder.encode("http://localhost:8083/user/naverlogin", "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SecureRandom random = new SecureRandom();
        String state = new BigInteger(130, random).toString();
        String apiURL = "https://nid.naver.com/oauth2.0/authorize?response_type=code";
        apiURL += "&client_id=" + clientId;
        apiURL += "&redirect_uri=" + redirectURL;
        apiURL += "&state=" + state;
        model.addAttribute(new User());
        model.addAttribute("apiURL", apiURL);
        session.getServletContext().setAttribute("state", state);
        session.getServletContext().setAttribute("session", session);
        System.out.println("1.session.id=" + session.getId());
        return "user/login";
    }

    @RequestMapping("naverlogin")
    public String naverLogin(String code, String state, HttpSession session) {
        System.out.println("2.session.id = " + session.getId());
        String clientId = "7QpF5ZIsnvhvX9aZqwYN";
        String clientSecret = "2vV8u728M4";
        String redirectURL = null;
        try {
            redirectURL = URLEncoder.encode("YOUR_CALLBACK_URL", "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String apiURL;
        apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&";
        apiURL += "client_id=" + clientId;
        apiURL += "&client_secret=" + clientSecret;
        apiURL += "&redirect_uri=" + redirectURL;
        apiURL += "&code=" + code;
        apiURL += "&state=" + state;
        System.out.println("code=" + code + ",state=" + state);
        StringBuffer res = new StringBuffer();
        System.out.println("apiURL : " + apiURL);
        try {
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            BufferedReader br;
            System.out.print("responseCode=" + responseCode);
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                res.append(inputLine);
            }
            br.close();
            if (responseCode == 200) {
                System.out.println(res.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(res.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String token = (String) json.get("access_token");
        String header = "Bearer " + token;
        try {
            apiURL = "https://openapi.naver.com/v1/nid/me";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", header);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            res = new StringBuffer();
            System.out.println("responseCode=" + responseCode);
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                res.append(inputLine);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            json = (JSONObject) parser.parse(res.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ShopException("네이버로그인시 오류발생", "login");
        }
        System.out.println("json=" + json);
        System.out.println("res=" + res);
        JSONObject jsondetail = (JSONObject) json.get("response");
        String email = jsondetail.get("email").toString();
        String userid = email.substring(0, email.indexOf("@"));
        User user = service.selectUser(userid);
        if (user == null) {
            user = new User();
            user.setUsername(jsondetail.get("name").toString());
            String phone = jsondetail.get("mobile").toString();
            user.setUserid(email.substring(0, email.indexOf("@")));
            user.setEmail(email);
            user.setPhoneno(phone);
            user.setChannel("templates/naver");
            service.userInsert(user);
        }
        session.setAttribute("loginUser", user);
        return "redirect:mypage?userid=" + user.getUserid();
    }

    @RequestMapping("logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }

    @PostMapping("login")
    public String login(User user, BindingResult bresult, Model model, HttpSession session) {
        model.addAttribute("title", "로그인");
        if (user.getUserid().trim().length() < 3 || user.getUserid().trim().length() > 10) {
            bresult.rejectValue("userid", "error.required.userid", "아이디는 3~10자리여야 합니다.");
        }
        if (user.getPassword().trim().length() < 3 || user.getPassword().trim().length() > 10) {
            bresult.rejectValue("password", "error.required.password", "비밀번호는 3~10자리여야 합니다.");
        }
        if (bresult.hasErrors()) {
            bresult.reject("error.input.check", "입력 값을 확인해 주세요.");
            return "user/login";
        }
        User dbUser = service.selectUser(user.getUserid());
        if (dbUser == null) {
            bresult.reject("error.login.id", "존재하지 않는 아이디입니다.");
            return "user/login";
        }
        if (CipherUtil.makehash(user.getPassword()).equals(dbUser.getPassword())) {
            session.setAttribute("loginUser", dbUser);
            return "redirect:/user/mypage?userid=" + user.getUserid();
        } else {
            bresult.reject("error.login.password", "비밀번호가 일치하지 않습니다.");
            return "user/login";
        }
    }

    @RequestMapping("mypage")
    public String idCheckMypage(@RequestParam("userid") String userid, Model model, HttpSession session) {
        model.addAttribute("title", "내 정보");
        User user = service.selectUser(userid);
        user = emailDecrpt(user);
        List<Sale> salelist = shopService.saleList(userid);
        model.addAttribute("user", user);
        model.addAttribute("salelist", salelist);

        return "user/mypage";
    }

    @GetMapping({"update", "delete"})
    public String idCheckUser(@RequestParam("userid") String userid, Model model, HttpServletRequest request, HttpSession session) {
        User user = service.selectUser(userid);
        user = emailDecrpt(user);
        String requestURI = request.getRequestURI();
        if (requestURI.endsWith("/user/update")) {
            model.addAttribute("title", "정보 수정");
            UserDto userDto = new UserDto();
            userDto.setUserid(user.getUserid());
            userDto.setUsername(user.getUsername());
            userDto.setPhoneno(user.getPhoneno());
            userDto.setPostcode(user.getPostcode());
            userDto.setAddress(user.getAddress());
            userDto.setEmail(user.getEmail());
            userDto.setBirthday(user.getBirthday());
            model.addAttribute("userDto", userDto);
            return "user/update";
        } else if (requestURI.endsWith("/user/delete")) {
            model.addAttribute("title", "회원 탈퇴");
            model.addAttribute("user", user); // 탈퇴 페이지에서는 User 객체를 직접 사용할 수 있음
            return "user/delete";
        } else {
            model.addAttribute("title", "페이지 오류");
            return "error/error";
        }
    }

    @PostMapping("update")
    public String idCheckUpdate(@Valid UserDto userDto, BindingResult bresult, Model model, HttpSession session,
                                @RequestParam("currentPassword") String currentPassword) {
        model.addAttribute("title", "정보 수정");
        if (bresult.hasErrors()) {
            model.addAttribute("userDto", userDto);
            return "user/update";
        }
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            throw new ShopException("세션이 만료되었습니다. 다시 로그인해주세요.", "/user/login");
        }
        if (!CipherUtil.makehash(currentPassword).equals(loginUser.getPassword())) {
            bresult.reject("error.login.password", "현재 비밀번호가 일치하지 않습니다.");
            model.addAttribute("userDto", userDto);
            return "user/update";
        }
        try {
            User userToUpdate = service.selectUser(userDto.getUserid());
            if (userToUpdate == null) {
                throw new ShopException("수정할 사용자 정보를 찾을 수 없습니다.", "/user/login");
            }
            userToUpdate.setUsername(userDto.getUsername());
            userToUpdate.setPhoneno(userDto.getPhoneno());
            userToUpdate.setPostcode(userDto.getPostcode());
            userToUpdate.setAddress(userDto.getAddress());
            userToUpdate.setBirthday(userDto.getBirthday());
            userToUpdate.setEmail(userDto.getEmail());
            emailEncrpt(userToUpdate);
            service.userUpdate(userToUpdate);
            session.setAttribute("loginUser", service.selectUser(userToUpdate.getUserid()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ShopException("고객 정보 수정 중 오류가 발생했습니다.", "/user/update?userid=" + userDto.getUserid());
        }
        return "redirect:/user/mypage?userid=" + userDto.getUserid();
    }

    @PostMapping("delete")
    public String idCheckDelete(@RequestParam("password") String password,
                                @RequestParam("userid") String userid,
                                HttpSession session) {
        if(userid.equals("admin")) {
            throw new ShopException("관리자는 탈퇴할 수 없습니다.", "/user/mypage?userid="+userid);
        }
        User userToDelete = service.selectUser(userid);
        if(userToDelete == null || !CipherUtil.makehash(password).equals(userToDelete.getPassword())) {
            throw new ShopException("비밀번호를 확인하세요.", "/user/delete?userid="+userid);
        }
        service.userDelete(userid);
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser != null && loginUser.getUserid().equals(userid)) {
            session.invalidate();
        }
        return "redirect:/user/login";
    }

    @GetMapping("password")
    public String loginCheckform(Model model) {
        model.addAttribute("title", "비밀번호 변경");
        return "user/password";
    }

    @PostMapping("password")
    public String loginCheckPassword(@RequestParam("password") String password, @RequestParam("chgpass") String chgpass, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            throw new ShopException("로그인 후 이용해 주세요.", "/user/login");
        }
        if (!CipherUtil.makehash(password).equals(loginUser.getPassword())) {
            throw new ShopException("현재 비밀번호가 일치하지 않습니다. 다시 확인해주세요.", "/user/password");
        }
        try {
            String makehash = CipherUtil.makehash(chgpass);
            service.updatePassword(loginUser.getUserid(), makehash);
            loginUser.setPassword(makehash);
            session.setAttribute("loginUser", loginUser);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ShopException("비밀번호 변경 중 오류가 발생했습니다.", "/user/password");
        }
        return "redirect:/user/mypage?userid=" + loginUser.getUserid();
    }

    private User emailDecrpt(User user) {
        String key = CipherUtil.makehash(user.getUserid()).substring(0, 16);
        String plainEmail = CipherUtil.decrypt(user.getEmail(), key);
        user.setEmail(plainEmail);
        return user;
    }

    private User emailEncrpt(User user) {
        String key = CipherUtil.makehash(user.getUserid()).substring(0, 16);
        String encryptedEmail = CipherUtil.encrypt(user.getEmail(), key);
        user.setEmail(encryptedEmail);
        return user;
    }
}
