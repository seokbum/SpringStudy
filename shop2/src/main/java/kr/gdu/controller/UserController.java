package kr.gdu.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.gdu.exception.ShopException;
import kr.gdu.logic.Sale;
import kr.gdu.logic.User;
import kr.gdu.service.ShopService;
import kr.gdu.service.UserService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.*;

@Controller
@RequestMapping("user")
public class UserController {


    @Autowired
    private UserService service;

    @Autowired
    private ShopService shopService;


    @GetMapping("*")
    public ModelAndView form() {
        ModelAndView mav = new ModelAndView();
        mav.addObject(new User());
        return mav;
    }

    @PostMapping("join")
    public ModelAndView joinForm(@Valid User user, BindingResult bresult) {
        ModelAndView mav = new ModelAndView();
        if (bresult.hasErrors()) {
            //추가 오류메시지 등록(globalErrors)
            bresult.reject("error.input.user");
            bresult.reject("error.input.check");
            return mav;
        }
        //정상입력
        try {
            service.userInsert(user);

        } catch (DataIntegrityViolationException e) {
            bresult.reject("error.duplicate.user");
            e.printStackTrace();
            return mav;
        } catch (Exception e) {
            System.err.println(e.getClass());
            return mav;
        }
        mav.setViewName("redirect:login");
        return mav;
    }

    /*
     *userid맞는 User를 DB에서조회
     *비밀번호검증
     *일치 : session.setAttribute("loginUser",dbUser)
     *mypage로 페이지이동
     *
     *불일치 : 비밀번호를 확인하세요 . 출력 error.login.password
     *
     *
     */
    @GetMapping("login")
    public String callLogin(HttpSession session, Model model) {
        String clientId = "네이버의 client ID";
        clientId = "7QpF5ZIsnvhvX9aZqwYN";
        String redirectURL = null;
        try {
            // 콜백 URL 설정 => 네이버에서 정상처리로 결정되면 호출해주는 URL을 알려줌
            redirectURL = URLEncoder.encode("http://localhost:8080/user/naverlogin", "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SecureRandom random = new SecureRandom();
        // 130 자리의 임의의 큰 정수값.
        String state = new BigInteger(130, random).toString();

        String apiURL = "https://nid.naver.com/oauth2.0/authorize?response_type=code";
        apiURL += "&client_id=" + clientId;
        apiURL += "&redirect_uri=" + redirectURL;
        apiURL += "&state=" + state;
        model.addAttribute(new User());
        model.addAttribute("apiURL", apiURL);
        session.getServletContext().setAttribute("session", session);
        System.out.println("1.session.id=" + session.getId());
        return null;
    }
    /*
     * 1. /WEB-INF/views/
     */

    @RequestMapping("naverlogin")
    public String naverLogin(String code, String state, HttpSession session) {
        System.out.println("2.session.id = " + session.getId());
        String clientId = "클라이언트ID값"; //애플리케이션 클라이언트ID값
        clientId = "7QpF5ZIsnvhvX9aZqwYN";
        String clientSecret = "클라이언트 시크릿값";//애플리케이션 seceret값
        clientSecret = "2vV8u728M4";
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
//		String access_token="";
//		String refresh_token = "";
        StringBuffer res = new StringBuffer();

        System.out.println("apiURL : " + apiURL);
        try {
            URL url = new URL(apiURL); // apiURL 연결 성공
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            // 네이버에서 전달한 응답 코드
            int responseCode = con.getResponseCode();
            BufferedReader br;
            System.out.print("responseCode=" + responseCode);
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
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
        //네이버의 응답은 json 형식
        //JSON형태의 문자열데이터 -> JSON객체로변경위함
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
            //user.setUserid(userid);
            user.setUsername(jsondetail.get("name").toString());
            String phone = jsondetail.get("mobile").toString();
            user.setUserid(email.substring(0, email.indexOf("@")));
            user.setEmail(email);
            user.setPhoneno(phone);
            user.setChannel("naver");
            service.userInsert(user);
        }
        session.setAttribute("loginUser", user);
        return "redirect:mypage?userid=" + user.getUserid();
    }


    @GetMapping("mypage")
    public ModelAndView idCheckMypage(String userid, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        //아이디를이용해 객체를 뽑음
        User user = service.selectUser(userid);

        List<Sale> salelist = shopService.saleList(userid);
        mav.addObject("user", user);
        mav.addObject("salelist", salelist);
        return mav;
    }

    @PostMapping("login")
    public ModelAndView login(User user, BindingResult bresult, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        if (user.getUserid().trim().length() < 3
                || user.getUserid().trim().length() > 10) {
            //@Valid 어노테이션을 사용하지않고  등록방식으로처리
            //messages.properties 파일에 : error.required.userid로 등록
            bresult.rejectValue("userid", "error.required");
        }
        if (user.getPassword().trim().length() < 3
                || user.getPassword().trim().length() > 10) {
            bresult.rejectValue("password", "error.required");
        }
        User dbUser = service.selectUser(user.getUserid());
        if (dbUser == null) { //아이디존재X
            bresult.reject("error.login.id");
            return mav;
        }
        if (user.getPassword().equals(dbUser.getPassword())) { //비밀번호일치
            session.setAttribute("loginUser", dbUser);
            //mypage에서 파라미터로넘긴 아이디를 이용해 유저검증 실시
            mav.setViewName("redirect:mypage?userid=" + user.getUserid());
        } else {
            bresult.reject("error.login.password");
            return mav;
        }
        return mav;
    }


    @RequestMapping("logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:login";
    }

    //해당 메서드도AOP방식으로로그인검증을 함 !!!
    //(하지만 하나의Mapping으로 두가지를 처리하는 방식은 비추임)
    @GetMapping({"update", "delete"})
    public ModelAndView idCheckUser(String userid, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        User user = service.selectUser(userid);
        mav.addObject(user);
        return mav;
    }

    @PostMapping("update")
    public ModelAndView idCheckUpdate(@Valid User user, BindingResult bresult,
                                      String userid, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        //입력값오류발생 시
        if (bresult.hasErrors()) {
            bresult.reject("error.update.user");
            return mav;
        }
        User loginUser = (User) session.getAttribute("loginUser");
        if (!loginUser.getPassword().equals(user.getPassword())) {
            bresult.reject("error.login.password");
            return mav;
        }
        //비밀번호일치시 실행
        try {
            service.userUpdate(user);
            if (loginUser.getUserid().equals(user.getUserid())) {
                //세션을 수정된 데이터로변경
                session.setAttribute("loginUser", user);
            }
            mav.setViewName("redirect:mypage?userid=" + user.getUserid());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ShopException("고객정보수정실패", "update?userid=" + user.getUserid());
        }
        return mav;
    }

    /*
     * 1.관리자인경우 탈퇴불가능
     * 2.	비밀번호검증 -> 로그인된비밀번호와비교
     * 		본인탈퇴시 : 본인비번
     * 		관리자 ->타인 탈퇴 : 관리자비번으로검증
     * 3.비밀번호불일치
     * 		메시지출력후 delete페이지로이동
     * 4.비밀번호일치
     * 		db에서 사용자정보삭제
     * 		본인탈퇴 : 로그아웃 . login페이지로이동
     * 		관리자 타인 탈퇴 : admin/list 페이지이동
     */
    @PostMapping("delete")
    public ModelAndView idCheckDelete(String password, String userid, HttpSession session) {
        ModelAndView mav = new ModelAndView();

        if (userid.equals("admin")) {
            throw new ShopException("관리자는 탈퇴할수없어요", "mypage?userid=" + userid);
        }

        User loginUser = (User) session.getAttribute("loginUser");
        if (!password.equals(loginUser.getPassword())) {
            throw new ShopException("비밀번호오류", "delete?userid=" + userid);
        }

        try {
            service.userDelete(userid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ShopException("탈퇴시 오류발생", "delete?userid=" + userid);
        }

        //탈퇴성공
        String url = null;
        if (loginUser.getUserid().equals("admin")) {
            url = "redirect:../admin/list";
        } else {
            session.invalidate();
            url = "redirect:login";
        }
        mav.setViewName(url);
        return mav;
    }

    /*
     * 본인비번만 변경가능
     * 1.로그인검증 -> AOP
     * 	UserLoginAspect.loginCheck()
     * 	poincut : UserController.loginCheck*(..)인 메서드,
     * 	마지막변수가 HttpSession인 메서드
     * advice : around
     *
     * 2. 현재 DB의 비밀번호와 파라미터 현재비밀번호일치 검증
     * 3.비밀번호일치  : DB수정 , 로그인정보변경 , mypage로이동
     * 4.비밀번호 불일치 : 오류메시지 출력 후 password창으로!
     *
     */

    @GetMapping("password")
    public String loginCheckForm(HttpSession session) {
        return null; //null로설정시 알아서 url을 맞춰줄거임
    }

    @PostMapping("password")
    public ModelAndView loginCheckPassword(@RequestParam String password,
                                           @RequestParam String chgpass, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        User loginUser = (User) session.getAttribute("loginUser");
        //User user = service.selectUser(loginUser.getUserid());
        if (!loginUser.getPassword().equals(password)) {
            throw new ShopException("비밀번호 불일치", "password");
        }
        try {
            loginUser.setPassword(chgpass);
            service.changePw(loginUser);
            session.setAttribute("loginUser", loginUser);
            mav.setViewName("redirect:mypage?userid=" + loginUser.getUserid());
            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ShopException("비밀번호변경시 DB오류입니다", "password");
        }
    }

    @PostMapping("{url}Search")
    public ModelAndView search(@ModelAttribute User user, BindingResult bresult,
                               @PathVariable String url) {
        System.out.println("@@url : " + url + "@@");
        //@PathVariable : {url}의 값을 매개변수로 전달
        //idSearch 요청 : url = id
        ModelAndView mav = new ModelAndView();
        String code = "error.userid.search";
        String title = "아이디";

        //요청url이 pwSearch라면 (아이디를 입력받을것임)
        if (url.equals("pw")) {
            title = "비밀번호 초기화";
            code = "error.password.search";
            if (user.getUserid() == null ||
                    user.getUserid().trim().equals("")) {
                bresult.rejectValue("userid", "error.required");
            }
        }
        //
        if (user.getEmail() == null || user.getEmail().trim().equals("")) {
            bresult.rejectValue("email", "error.required");
        }
        if (user.getPhoneno() == null || user.getPhoneno().trim().equals("")) {
            bresult.rejectValue("phoneno", "error.required");
        }
        if (bresult.hasErrors()) {
            bresult.reject("error.input.check");
            return mav;
        }

        if (user.getUserid() != null && user.getUserid().trim().equals("")) {
            //modelAttribute로넘겨받은 user의 아이디값이 빈 값이라면?(idSearch에서넘어온경우)
            user.setUserid(null);
        }
        String result = service.getSearch(user);
        System.out.println("result : " + result);
        if (result == null) {
            bresult.reject(code);
            return mav;
        }
        //url정보가 pw면 무작위비밀번호로 업데이트
        if (url.equals("pw")) {
            user.setPassword(getTempPw());
            service.changePw(user);
            result = user.getPassword();
        }

        mav.addObject("result", result);
        mav.addObject("title", title);
        mav.setViewName("search");
        return mav;
    }

    //비밀번호초기화알고리즘
    public String getTempPw() {
        List<String> lowerList = Arrays.asList
                ("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"
                        , "l", "m", "n", "o", "p", "q", "r", "s", "t", "z");

        List<String> upperList = new ArrayList<>();
        for (String string : lowerList) {
            upperList.add(string.toUpperCase());
        }
        List<Object> combineList = new ArrayList<>();
        combineList.addAll(lowerList);
        combineList.addAll(upperList);
        for (int i = 0; i < 30; i++) { //랜덤한0~9 숫자 30개집어넣기
            combineList.add(new Random().nextInt(10));
        }
        //무작위 섞기
        Collections.shuffle(combineList);
        String tempNum = "";
        for (int i = 0; i < 6; i++) {
            int num = new Random().nextInt(combineList.size());
            tempNum += combineList.get(num);
        }
        return tempNum;
    }
}
