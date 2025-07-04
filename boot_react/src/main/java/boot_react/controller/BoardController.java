package boot_react.controller;

import boot_react.dto.BoardDto;
import boot_react.entity.BoardEntity;
import boot_react.service.BoardService;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/board/")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class BoardController {
    @Autowired
    BoardService boardService;

    @Value("${image.upload.dir}")
    private String PATH;

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
        String boardName = switch (boardid) {
            case "1" -> "공지사항";
            case "2" -> "자유게시판";
            case "3" -> "Q&A";
            default -> "알 수 없는 게시판";
        };
        int limit = 10;
        int listcount = boardService.boardCount(boardid);
        List<BoardEntity> blist = boardService.boardList(pageInt, limit, boardid).getContent();
        int bottomLine = 10;
        int start = (pageInt - 1) / bottomLine * bottomLine + 1;
        int end = start + bottomLine - 1;
        int maxPage = (listcount / limit) + (listcount % limit == 0 ? 0 : 1);
        if (end > maxPage) {
            end = maxPage;
        }
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
    public Map<String, Object> boardPro(
                                         @RequestParam(value = "file2", required = false) MultipartFile multipartFile,
                                         BoardDto boardDto,
                                         HttpServletRequest request,
                                         ServletResponse servletResponse) {
        String path = PATH + "/img/board/";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = "";
        if (multipartFile != null && !multipartFile.isEmpty()) {
            fileName = multipartFile.getOriginalFilename();
            File file = new File(path, fileName);
            try {
                multipartFile.transferTo(file);
            } catch (Exception e) {
                e.printStackTrace();
                return Map.of("code", -1, "msg", "파일 업로드 실패");
            }
        }
        boardDto.setBoardid(boardDto.getBoardid());
        boardDto.setFile1(fileName);
        BoardEntity num = boardService.insertBoard(new BoardEntity(boardDto));
        return Map.of("code", 0, "msg", "게시물 등록 성공", "num", num.getNum());
    }

    @GetMapping("boardInfo")
    public Map<String, Object> boardInfo(@RequestParam("num") int num) {
        BoardEntity board = boardService.getBoard(num);
        boardService.addReadcnt(num);
        String boardName = switch (board.getBoardid()) {
            case "1" -> "공지사항";
            case "2" -> "자유게시판";
            case "3" -> "Q&A";
            default -> "알 수 없는 게시판";
        };
        return Map.of(
                "board", board,
                "boardName", boardName);
    }

    @GetMapping("boardUpdateForm")
    public Map<String, Object> boardUpdateForm(@RequestParam("num") int num) {
        BoardEntity board = boardService.getBoard(num);
        String boardName = switch (board.getBoardid()) {
            case "1" -> "공지사항";
            case "2" -> "자유게시판";
            case "3" -> "Q&A";
            default -> "알 수 없는 게시판";
        };
        return Map.of(
                "board", board,
                "boardName", boardName);
    }

    @PostMapping("boardUpdatePro")
    public Map<String, Object> boardUpdatePro(@RequestParam (value = "file2", required = false) MultipartFile multipartFile,BoardDto boardDto) throws IllegalStateException, IOException {
        BoardEntity dbBoard = boardService.getBoard(boardDto.getNum());
        Map<String,Object> map = new HashMap<>();
        if (!boardDto.getPass().equals(dbBoard.getPass())) {
            map.put("msg","비밀번호 오류");
            map.put("code",100);
            return map;
        }
        // 입력값 정상 비밀번호 일치
        String path = PATH + "/img/board/";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = "";
        if (multipartFile != null && !multipartFile.isEmpty()) {
            File file = new File(path, multipartFile.getOriginalFilename());
            fileName = multipartFile.getOriginalFilename();
            multipartFile.transferTo(file);
            boardDto.setFile1(fileName);
        }else {
            boardDto.setFile1(dbBoard.getFile1());
        }try {
            boardDto.setRegdate(dbBoard.getRegdate());
            boardService.boardUpdate(new BoardEntity(boardDto));
            map.put("msg","게시글 수정 완료");
            map.put("code",0);
        }catch (Exception e) {
            map.put("msg","게시글 수정에 실패 했습니다");
            map.put("code",200);
            e.printStackTrace();
        }
        return map;
    }
}