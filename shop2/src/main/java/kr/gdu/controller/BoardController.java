package kr.gdu.controller;

import java.util.List;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.gdu.dto.board.BoardDetailDto;
import kr.gdu.dto.board.DeleteBoardDto;
import kr.gdu.exception.ShopException;
import kr.gdu.logic.Board;
import kr.gdu.logic.Comment;
import kr.gdu.service.BoardService;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("board")
public class BoardController {

    private final ItemController itemController;

    private final UserController userController;
	@Autowired
	private BoardService service;

    BoardController(UserController userController, ItemController itemController) {
        this.userController = userController;
        this.itemController = itemController;
    }
	
	@GetMapping("*")
	public ModelAndView write() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new Board());
		return mav;
	}
	/* Spring에서 파라미터 전달 방식
	 *   1. 파라미터이름과 매개변수의 이름이 같은 경우 매핑
	 *   2. Bean 클래스의 프로퍼티명과 파라미터이름이 같은 경우 매핑
	 *   3. Map 객체에 RequestParam 어노테이션을 이용한 매핑
	 * 
	 * @RequestParam : 파라미터값을 Map 객체에 매핑하여 전달
	 */
	@RequestMapping("list")
	public String list(@RequestParam Map<String,String> param,
			 HttpSession session,Model model) {
		int pageNum = 0;
		for(String key : param.keySet()) {
			if(param.get(key) == null || param.get(key).trim().equals("")) {
			   param.put(key, null);	
			}
		}
		if (param.get("pageNum") != null) {
			   pageNum = Integer.parseInt(param.get("pageNum"));
		} else { 
			pageNum = 1;
		}			
		String boardid = param.get("boardid");
		//StringUtils.hasText("x") : x가 빈값이면 false
		if(!StringUtils.hasText(boardid)) {
			boardid= "1";
		}
		String searchtype = param.get("searchtype");
		String searchcontent = param.get("searchcontent");		
		String boardName = null;
		switch(boardid) {
		   case "1" -> boardName = "공지사항";
		   case "2" -> boardName = "자유게시판"; 
		   case "3" -> boardName = "QNA";
		}
		//게시판 조회 처리
		int limit = 10; //한 페이지의 최대게시물
		//listcount : boardid별 전체 게시물건수
		int listcount = service.boardcount(boardid,searchtype,searchcontent); 
		List<Board> boardlist = service.boardlist
				          (pageNum,limit,boardid,searchtype,searchcontent);
		
		//페이징 처리를 위한 변수
		int maxpage = (int)((double)listcount/limit + 0.95);
		//startpage : 현재 화면에 보여질 시작페이지 1 or 11
		int startpage = (int)((pageNum/10.0 + 0.9) - 1) * 10 + 1;
		//endpage : 현재화면에보여질 마지막 페이지 값 
		int endpage = startpage + 9;
		if(endpage > maxpage) { 
			endpage = maxpage;
		}
		int boardno = listcount - (pageNum - 1) * limit;		
		model.addAttribute("boardid",boardid);
		model.addAttribute("boardName",boardName);
		model.addAttribute("pageNum",pageNum);
		model.addAttribute("maxpage",maxpage);//14개의글이있다면 maxpage:2
		model.addAttribute("startpage",startpage);//기본값 : 1
		model.addAttribute("endpage",endpage);
		model.addAttribute("listcount",listcount);
		model.addAttribute("boardlist",boardlist);
		model.addAttribute("boardno",boardno);
		return "board/list";
	}
	
	@GetMapping("detail")
	public String detail(@RequestParam int num,Model model ) {
		Board board = service.getBoard(num);
		service.addReadcnt(num);
		
		String boardName = null;
		String boardid = board.getBoardid();
		if(boardid==null) {
			boardid="1";
		}
		switch(board.getBoardid()) {		
		   case "1" -> boardName = "공지사항";
		   case "2" -> boardName = "자유게시판"; 
		   case "3" -> boardName = "QNA";
		}
		
		
		//댓글목록 화면에전달
		List<Comment> commlist = service.commentList(num);
		Comment comm = new Comment();
		comm.setNum(num);
		
		model.addAttribute("board",board);
		model.addAttribute("boardName",boardName);
		model.addAttribute("commlist",commlist);
		model.addAttribute("comment",comm);
		
		return "board/detail";
	}
	
	@PostMapping("write")
	public String writePost(@Valid Board board , BindingResult bresult,
			HttpServletRequest request) {
		if(bresult.hasErrors()) {
			// /WEB-INF/views/board/write.jsp 페이지로
			return "board/write";
		}
		if(!StringUtils.hasText(board.getBoardid())) {
			board.setBoardid("1");
		}
		service.boardWrite(board,request);
		return "redirect:list?boardid="+board.getBoardid();
	}
	
	@GetMapping({"update","reply","delete"})
	public ModelAndView getBoard(@RequestParam int num, @RequestParam String boardid ,Model model) {
		ModelAndView mav = new ModelAndView();
		Board board = service.getBoard(num);
		mav.addObject("board",board);
		if(boardid==null || boardid.equals("1")) {
			mav.addObject("boardName","공지사항");
		}
		else if(boardid.equals("2")) {
			mav.addObject("boardName","자유게시판");
		}
		else if(boardid.equals("3")) {
			mav.addObject("boardName","Q&A");
		}
		mav.addObject("num",num);
		mav.addObject("boardid",boardid);
		return mav;
	}
	
	@PostMapping("update")
	public String update(@Valid Board board , BindingResult bresult
			,Model model,HttpServletRequest request) {
		if(bresult.hasErrors()) {
			return "board/update";
		}
		Board dbBoard = service.getBoard(board.getNum());
		if(!board.getPass().equals(dbBoard.getPass())) {
			throw new ShopException("비밀번호가틀립니다", "update?num="+board.getNum()+
					"&boardid="+dbBoard.getBoardid());
		}
		//입력값 정상+비밀번호일치
		try {
			service.boardUpdate(board,request);
			return "redirect:detail?num="+board.getNum();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new ShopException("업데이트실패", "update?num="+board.getNum()+
					"&boardid="+dbBoard.getBoardid());
		}
	}
	
	@PostMapping("delete")
	public String delete( 
			@Valid @ModelAttribute DeleteBoardDto dto,BindingResult bresult,Model model) {
		
		Board dbBoard = service.getBoard(dto.getNum());
		if(!dbBoard.getPass().equals(dto.getPass())) {
			bresult.reject("error.dto.pass");
			model.addAttribute("num",dto.getNum());
			model.addAttribute("boardid",dto.getBoardid());
			return "board/delete";
		}
		
		service.boardDelete(dto);
		return "redirect:list?boardid="+dbBoard.getBoardid();		
	}
	
	
	
	@PostMapping("reply")
	public String callReply(@Valid Board board, BindingResult bresult,Model model) {
		System.out.println("board :: "+board);
		String boardid = board.getBoardid();
		int num = board.getNum();
		if(bresult.hasErrors()) {
			model.addAttribute("num",num);
			model.addAttribute("boardid",boardid);
			return "board/reply";	
		}
		try {
			service.boardReply(board);
			return "redirect:list?boardid="+boardid;
		}
		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("num",num);
			model.addAttribute("boardid",boardid);
			String url = "board/reply";
			throw new ShopException("오류발생", url);
			}
	}
	
	@PostMapping("comment")
	public String comment(@Valid @ModelAttribute Comment comm,BindingResult bresult,Model model) {
		if(bresult.hasErrors()) {
			return commDetail(comm, model);
		}
		//comment테이블의 기본키값 , num,seq
		int seq = service.comMaxSeq(comm.getNum());
		comm.setSeq(++seq);
		service.comInsert(comm);
		return "redirect:detail?num="+comm.getNum()+"#comment";
		//#comment : id속성값이 comment위치로 시작
	}
	
	private String commDetail(Comment comm,Model model) {
		int num = comm.getNum();
		Board board = service.getBoard(num);		
		String boardName = null;
		String boardid = board.getBoardid();
		if(boardid==null) {
			boardid="1";
		}
		switch(board.getBoardid()) {		
		   case "1" -> boardName = "공지사항";
		   case "2" -> boardName = "자유게시판"; 
		   case "3" -> boardName = "Q&A";
		}	
		//댓글목록 화면에전달
		List<Comment> commlist = service.commentList(num);
		comm.setNum(num);
		
		model.addAttribute("board",board);
		model.addAttribute("boardName",boardName);
		model.addAttribute("commlist",commlist);
		model.addAttribute("comment",comm);
		return "board/detail";
	}
	
	@PostMapping("commdel")
	public String comment(@ModelAttribute Comment comm,Model model) {
		Comment dbComm = service.commSelectOne(comm.getNum(),comm.getSeq());
		if(comm.getPass().equals(dbComm.getPass())) {
			service.commDel(comm.getNum(),comm.getSeq());
		}
		else {
			throw new ShopException("댓글삭제실패", "detail?num="+comm.getNum()+"#comment");
		}
		return "redirect:detail?num="+comm.getNum()+"#comment";
	}
	
	
}