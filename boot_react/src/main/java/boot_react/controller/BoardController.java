package boot_react.controller;

import boot_react.entity.BoardEntity;
import boot_react.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/board/")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
//allowCredentials = "true" : 인증의 요청도 허용
public class BoardController {
    @Autowired
    BoardService boardService;

    @GetMapping("boardList")
    public Map<String, Object> boardList(@RequestParam Map<String, String> param) {
        Integer pageInt = null;
        for (String key : param.keySet()) {
            if (param.get(key) == null || param.get(key).trim().equals("")) {
                param.put(key, null);
            }
        }
        if (param.get("page") != null) {
            pageInt = Integer.parseInt(param.get("page"));
        } else {
            pageInt = 1;
        }
        String boardid = param.get("boardid");
        if (boardid == null) {
            boardid = "1";
        }
        String boardName = null;
        switch (boardid) {
            case "1":
                boardName = "공지사항";
                break;
            case "2":
                boardName = "자유게시판";
                break;
            case "3":
                boardName = "Q&A";
                break;
        }
        int limit = 10;
        int listcount = boardService.boardCount(boardid); // 전체 게시물 등록 건수
        // 해당 페이지에 출력할 게시물 목록
        List<BoardEntity> blist = boardService.boardList(pageInt, limit, boardid).getContent();
        // 게시판 페이징에 필요한 변수 설정
        int bottomLine = 10; // 한페이지 보여질 페이지 번호 갯수
        int start = (pageInt - 1) / bottomLine * bottomLine + 1; // 시작페이지 번호
        int end = start + bottomLine - 1; // 마지막 페이지 번호
        int maxPage = (listcount / limit) + (listcount % limit == 0 ? 0 : 1); // 최대 페이지
        if (end > maxPage) {
            end = maxPage;
        }
        /*
            Map 객체 생성
            1.  Map <k,v> map = new HashMap<>();
                map.put("k","v")...
            2. Map.op(...)
               Java 9에 추가됨
               변경불가
         */
        return Map.of(
                "boardid", boardid,
                "boardName", boardName,
                "pageInt", pageInt,
                "maxPage", maxPage,
                "start", start,
                "end", end,
                "listcount", listcount,
                "blist", blist,
                "bottomLine", bottomLine);
    }

    @PostMapping("boardPro")
    public Map<String, Object> boardPro(@RequestParam Map<String, String> param) {
        System.out.println("param = " + param);
        return Map.of();
    }
}
