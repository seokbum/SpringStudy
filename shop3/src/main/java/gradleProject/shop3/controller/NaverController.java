package gradleProject.shop3.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("naver")
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 만들어줍니다.
public class NaverController {

    // application.properties에서 네이버 API 키를 안전하게 주입받습니다.
    @Value("${naver.client.id}")
    private String naverId;

    @Value("${naver.client.secret}")
    private String naverSecret;

    /**
     * '/naver/search' 주소로 접속했을 때, 사용자에게 보여줄 검색 페이지(HTML)를 반환합니다.
     */
    @GetMapping("/search")
    public String searchPage() {
        // "templates/naver/search.html" 파일을 찾아서 보여줍니다.
        return "naver/search";
    }

    /**
     * search.html 페이지의 검색 버튼을 눌렀을 때, JavaScript(AJAX)에 의해 호출됩니다.
     * 네이버 API에 실제 검색을 요청하고 결과를 JSON 형태의 문자열로 반환합니다.
     */
    @PostMapping(value = "/naversearch", produces = "application/json; charset=UTF-8")
    @ResponseBody // 이 메서드의 반환값은 HTML 페이지가 아니라 데이터 그 자체임을 명시합니다.
    public String naverSearchApi(String data, Integer display, Integer start, String type) {
        String text;
        try {
            // 검색어는 URL에 포함되므로 UTF-8로 인코딩해야 합니다.
            text = URLEncoder.encode(data, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "{\"error\":\"검색어 인코딩 실패\"}";
        }

        String apiURL = "https://openapi.naver.com/v1/search/" + type + ".json?query=" + text + "&display=" + display + "&start=" + start;

        try {
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", naverId);
            con.setRequestProperty("X-Naver-Client-Secret", naverSecret);

            int responseCode = con.getResponseCode();
            BufferedReader br;

            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8));
            }

            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            // 네이버에서 받은 JSON 데이터를 그대로 클라이언트(브라우저)에게 전달합니다.
            return response.toString();
        } catch (Exception e) {
            return "{\"error\":\"API 요청 실패: " + e.getMessage() + "\"}";
        }
    }
}