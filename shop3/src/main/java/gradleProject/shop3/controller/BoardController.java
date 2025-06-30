package gradleProject.shop3.controller;

import gradleProject.shop3.domain.Board;
import gradleProject.shop3.domain.Comment;
import gradleProject.shop3.dto.BoardDto;
import gradleProject.shop3.exception.ShopException;
import gradleProject.shop3.mapper.BoardMapper;
import gradleProject.shop3.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardMapper boardMapper;

    // 게시물 목록
    @GetMapping("list")
    public String list(@RequestParam(value = "boardid", defaultValue = "1") String boardid,
                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                       @RequestParam(value = "searchtype", required = false) String searchtype,
                       @RequestParam(value = "searchcontent", required = false) String searchcontent,
                       Model model) {
        int limit = 10;
        Page<BoardDto> paging = boardService.boardlist(pageNum, limit, boardid, searchtype, searchcontent);
        int maxpage = paging.getTotalPages();
        int startpage = (pageNum - 1) / limit * limit + 1;
        int endpage = startpage + 9;
        if (endpage > maxpage) endpage = maxpage;

        model.addAttribute("paging", paging);
        model.addAttribute("boardlist", paging.getContent());
        model.addAttribute("boardid", boardid);
        model.addAttribute("boardName", getBoardName(boardid));
        model.addAttribute("boardno", paging.getTotalElements() - (pageNum - 1) * limit);
        model.addAttribute("startpage", startpage);
        model.addAttribute("endpage", endpage);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("maxpage", maxpage);
        model.addAttribute("searchtype", searchtype);
        model.addAttribute("searchcontent", searchcontent);
        return "board/list";
    }

    // 게시물 상세
    @GetMapping("detail")
    public String detail(@RequestParam int num, Model model) {
        boardService.addReadcnt(num);
        Board board = boardService.getBoard(num);
        if (board == null) throw new ShopException("게시물을 찾을 수 없습니다.", "list?boardid=1");

        List<Comment> commlist = boardService.commentList(num);
        Comment comment = new Comment();
        comment.setNum(num);

        model.addAttribute("board", board);
        model.addAttribute("boardName", getBoardName(board.getBoardid()));
        model.addAttribute("commlist", commlist);
        model.addAttribute("comment", comment);
        return "board/detail";
    }

    // 글쓰기 폼
    @GetMapping("write")
    public String writeForm(@RequestParam String boardid, Model model) {
        BoardDto boardDto = new BoardDto();
        boardDto.setBoardid(boardid);
        model.addAttribute("boardDto", boardDto);
        model.addAttribute("boardName", getBoardName(boardid));
        return "board/write";
    }

    // 글쓰기 처리
    @PostMapping("write")
    public String writePost(@Valid @ModelAttribute("boardDto") BoardDto dto, BindingResult bresult, Model model) {
        if (bresult.hasErrors()) {
            model.addAttribute("boardName", getBoardName(dto.getBoardid()));
            return "board/write";
        }
        boardService.boardWrite(dto);
        return "redirect:/board/list?boardid=" + dto.getBoardid();
    }

    // 수정 폼
    @GetMapping("update")
    public String updateForm(@RequestParam int num, Model model) {
        Board board = boardService.getBoard(num);
        if (board == null) throw new ShopException("수정할 게시물이 없습니다.", "list?boardid=1");
        model.addAttribute("boardDto", boardMapper.toDto(board));
        model.addAttribute("boardName", getBoardName(board.getBoardid()));
        return "board/update";
    }

    // 수정 처리
    @PostMapping("update")
    public String updatePost(@Valid @ModelAttribute("boardDto") BoardDto dto, BindingResult bresult, Model model) {
        if (bresult.hasErrors()) {
            model.addAttribute("boardName", getBoardName(dto.getBoardid()));
            return "board/update";
        }
        Board dbBoard = boardService.getBoard(dto.getNum());
        if (!dbBoard.getPass().equals(dto.getPass())) {
            bresult.reject("error.password", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("boardName", getBoardName(dto.getBoardid()));
            return "board/update";
        }
        boardService.boardUpdate(dto);
        return "redirect:/board/detail?num=" + dto.getNum();
    }

    // 삭제 폼
    @GetMapping("delete")
    public String deleteForm(@RequestParam int num, Model model) {
        Board board = boardService.getBoard(num);
        if (board == null) throw new ShopException("삭제할 게시물이 없습니다.", "list?boardid=1");
        model.addAttribute("board", board);
        model.addAttribute("boardName", getBoardName(board.getBoardid()));
        return "board/delete";
    }

    // 삭제 처리
    @PostMapping("delete")
    public String deletePost(@RequestParam int num, @RequestParam String pass) {
        Board dbBoard = boardService.getBoard(num);
        if (!dbBoard.getPass().equals(pass)) {
            throw new ShopException("비밀번호가 일치하지 않습니다.", "delete?num=" + num);
        }
        boardService.boardDelete(num);
        return "redirect:/board/list?boardid=" + dbBoard.getBoardid();
    }

    // 답글 폼
    @GetMapping("reply")
    public String replyForm(@RequestParam int num, Model model) {
        Board parent = boardService.getBoard(num);
        if (parent == null) throw new ShopException("답글을 작성할 게시물이 없습니다.", "list?boardid=1");
        BoardDto dto = new BoardDto();
        dto.setBoardid(parent.getBoardid());
        dto.setGrp(parent.getGrp());
        dto.setGrplevel(parent.getGrplevel());
        dto.setGrpstep(parent.getGrpstep());
        dto.setTitle("RE: " + parent.getTitle());
        model.addAttribute("boardDto", dto);
        model.addAttribute("boardName", getBoardName(parent.getBoardid()));
        return "board/reply";
    }

    // 답글 처리
    @PostMapping("reply")
    public String replyPost(@Valid @ModelAttribute("boardDto") BoardDto dto, BindingResult bresult, Model model) {
        if (bresult.hasErrors()) {
            model.addAttribute("boardName", getBoardName(dto.getBoardid()));
            return "board/reply";
        }
        boardService.boardReply(dto);
        return "redirect:/board/list?boardid=" + dto.getBoardid();
    }

    // 댓글 등록
    @PostMapping("comment")
    public String addComment(@Valid Comment comment, BindingResult bresult, Model model) {
        if (bresult.hasErrors()) {
            Board board = boardService.getBoard(comment.getNum());
            model.addAttribute("board", board);
            model.addAttribute("commlist", boardService.commentList(comment.getNum()));
            model.addAttribute("comment", comment);
            model.addAttribute("boardName", getBoardName(board.getBoardid()));
            return "board/detail";
        }
        boardService.comInsert(comment);
        return "redirect:/board/detail?num=" + comment.getNum() + "#comment";
    }

    // 댓글 삭제
    @PostMapping("commdel")
    public String deleteComment(Comment comm) {
        Comment dbComm = boardService.commSelectOne(comm.getNum(), comm.getSeq());
        if (dbComm == null || !dbComm.getPass().equals(comm.getPass())) {
            throw new ShopException("비밀번호가 틀려 댓글을 삭제할 수 없습니다.", "detail?num=" + comm.getNum() + "#comment");
        }
        boardService.commDel(comm.getNum(), comm.getSeq());
        return "redirect:/board/detail?num=" + comm.getNum() + "#comment";
    }

    private String getBoardName(String boardid) {
        return switch (boardid) {
            case "1" -> "공지사항";
            case "2" -> "자유게시판";
            case "3" -> "Q&A";
            default -> "게시판";
        };
    }
}