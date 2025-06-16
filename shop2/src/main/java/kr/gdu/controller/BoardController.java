package kr.gdu.controller;

import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.gdu.exception.ShopException;
import kr.gdu.logic.Board;
import kr.gdu.logic.Comment;
import kr.gdu.service.BoardService;

@Controller
@RequestMapping("board")
public class BoardController {

	@Autowired
	private BoardService service;

	
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
	public ModelAndView list(@RequestParam Map<String,String> param,
			 HttpSession session) {
//		System.out.println("param:" + param);
		
		Integer pageNum = null;
		
		for(String key : param.keySet()) {
			if(param.get(key) == null || param.get(key).trim().equals("")) {
			   param.put(key, null);	
			}
		}
		
		if (StringUtils.hasText(param.get("pageNum"))) {
			   pageNum = Integer.parseInt(param.get("pageNum"));
		} else { 
			pageNum = 1;
		}
		
		String boardid = param.get("boardid");
		if(boardid == null) {
			boardid = "1";
		}
		String searchtype = param.get("searchtype");
		String searchcontent = param.get("searchcontent");
		
		ModelAndView mav = new ModelAndView();
		String boardName = null;
		
		switch(boardid) {
		   case "1" : boardName = "공지사항"; break;
		   case "2" : boardName = "자유게시판"; break;
		   case "3" : boardName = "Q&A"; break;
		}
		
		//게시판 조회 처리
		int limit = 10; // 한페이지 출력될 게시물 건수
		// listcount = boardid 별 전체 게시물 건수. 
		int listcount = service.boardcount(boardid,searchtype,searchcontent); 
		//boardlist = 해당 페이지 출력될 게시물 목록
		List<Board> boardlist = service.boardlist
				          (pageNum,limit,boardid,searchtype,searchcontent);
		
		// 페이징 처리를 위한 변수
		// 게시물 건수에 따른 최대 페이지값
		int maxpage = (int)((double)listcount/limit + 0.95);
		// startpage : 현재 화면에 보여질 시작 페이지값
		int startpage = (int)((pageNum/10.0 + 0.9) - 1) * 10 + 1;
		// endpage : 현재 화면에 보여질 마지막 페이지값
		int endpage = startpage + 9;
		// 마지막 페이지 값을 최대 페이지보다 클 수없다
		
		if(endpage > maxpage) {
			endpage = maxpage;
		}
		// 화면에 보여질 게시물번호
		int boardno = listcount - (pageNum - 1) * limit;
		
		mav.addObject("boardid",boardid);  
		mav.addObject("boardName", boardName); 
		mav.addObject("pageNum", pageNum); 
		mav.addObject("maxpage", maxpage); 
		mav.addObject("startpage", startpage);
		mav.addObject("endpage", endpage); 
		mav.addObject("listcount", listcount);
		mav.addObject("boardlist", boardlist);
		mav.addObject("boardno", boardno);		
		return mav;
	}
	
	@GetMapping("detail")
	public ModelAndView detail(@RequestParam Map<String,String> param) {
		
		ModelAndView mav = new ModelAndView("board/detail");
		Integer num = Integer.parseInt(param.get("num"));
		Board board = service.getBoard(num);
		service.addReadcnt(num);
		
		if(board.getBoardid() == null || board.getBoardid().equals("1")) {
			mav.addObject("boardName", "공지사항");
		}else if(board.getBoardid().equals("2")){
			mav.addObject("boardName", "자유게시판");
		}else if(board.getBoardid().equals("3")) {
			mav.addObject("boardName", "Q&A");
		}
		
		// 댓글 목록 화면에 전달
		// commlist : num 게시물의 댓글 목록
		List<Comment> commlist = service.commentlist(num);
		// 유효성 검증에 필요한 Comment 객체
		Comment comm = new Comment();
		comm.setNum(num);
		mav.addObject("board",board);
		mav.addObject("commlist",commlist);
		mav.addObject(comm);
		
		return mav;
	}
	
	@PostMapping("write")
	public ModelAndView writerPost(@Valid Board board, BindingResult bresult,
			HttpServletRequest request) {
		
		ModelAndView mav = new ModelAndView();
		
		if(bresult.hasErrors()) {
			return mav;
		}
		if(!StringUtils.hasText(board.getBoardid())) {
			board.setBoardid("1");
		}
		service.boardWrite(board,request);
		mav.setViewName("redirect:list?boardid="+board.getBoardid());
		
		return mav;
	}
	
	@GetMapping({"reply","update","delete"})
	public ModelAndView getBoard(Integer num, String boardid) {
		
		ModelAndView mav = new ModelAndView();
		Board board = service.getBoard(num);
		mav.addObject("board", board);
		
		if(boardid == null || boardid.equals("1")) {
			mav.addObject("boardName","공지사항");
		} else if (boardid.equals("2")) {
			mav.addObject("boardName", "자유게시판");
		} else if (boardid.equals("3")) {
			mav.addObject("boardName", "Q&A");
		}
		
		return mav;
	}
	
	@PostMapping("update")
	public ModelAndView updetePost(@Valid Board board, BindingResult bresult, 
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		
		if(bresult.hasErrors()) {
			return mav;
		}
		Board dbBoard = service.getBoard(board.getNum());
		
		if(!board.getPass().equals(dbBoard.getPass())) {
			throw new ShopException("비밀번호가 틀립니다.", "update?num="+board.getNum()
				+"&boardid="+dbBoard.getBoardid());
		}
		try {
			service.boardUpdate(board,request);
			mav.setViewName("redirect:detail?num="+board.getNum());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ShopException("게시글 수정에 실패 했습니다.", "update?num="+board.getNum()
				+"&boardid="+dbBoard.getBoardid());
		}
		
		return mav;
	}
	
	@PostMapping("delete")
	public ModelAndView deletePost(@Valid Board board, BindingResult bresult,
			HttpServletRequest request) {
	    ModelAndView mav = new ModelAndView();

	    Board dbBoard = service.getBoard(board.getNum());
	    
	    if (!board.getPass().equals(dbBoard.getPass())) {
	        bresult.reject("error.board.pass");
	        return mav;
	    }

	    try {
	        service.boardDelete(board.getNum());
	        mav.setViewName("redirect:list?boardid=" + dbBoard.getBoardid());
	    } catch (Exception e) {
	        e.printStackTrace();
	        bresult.reject("error.board.delete");
	        mav.addObject("board", dbBoard); 
	        return mav;
	    }

	    return mav;
	}
	/*
	 * 1. 유효성 검사 하기 - 파마미터 값 저장.
	 * 	- 원글 정보 : num,grp,grplevel,grpstep,boardid
	 * 	- 답글 정보 : writer,pass,title,content
	 * 2. db에 insert => service.boardReply()
	 * 	- 원글의 grpstep 보다 큰 기종 등록된 답글의 grpstep 값을 +1 수정
	 * 	=>board.grpStepAdd()
	 * 	- num : maxNum()+1
	 *  - db에 insert => boardDao.insert()
	 *  	grp : 원글과 동일
	 *  	grplevel : 원글 grplevel+1
	 *  	grpstep : 원글 grpstep+1
	 * 	3. 등록성공: list로 페이지 이동
	 * 	   등록 실패 : "답변 등록시 오류 발생 " reply 페이지 이동
	 */
	@PostMapping("reply")
	public ModelAndView replyPost(@Valid Board board, BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		
		if(bresult.hasErrors()) {
			return mav;
		}
		
		try {
			service.boardReply(board);
			mav.setViewName("redirect:list?boardid="+board.getBoardid());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ShopException("답변등록시 오류 발생.", "reply?num="+board.getNum()
			+"&boardid="+board.getBoardid());
		}
		return mav;
	}
	
	@RequestMapping("comment") // 댓글 등록
	public ModelAndView comment(@Valid Comment comm, BindingResult bresult) {
		
		ModelAndView mav = new ModelAndView("board/detail");
		
		if(bresult.hasErrors()) {
			return commdeteil(comm); // 입력 오류시, 정상적으로 조회 되도록 수정
		}
		//comment 테이블의 기본값 : num,seq
		int seq = service.commmaxseq(comm.getNum()); // num 게시글 중 최대 seq 값
		comm.setSeq(++seq);
		service.comminsert(comm); //comment 테이블에 추가
		mav.setViewName("redirect:detail?num="+comm.getNum()+"#comment");
		return mav;
	}
	
	public ModelAndView commdeteil(Comment comm) {
		ModelAndView mav = new ModelAndView("board/detail");
		
		Board board = service.getBoard(comm.getNum());
		
		if(board.getBoardid() == null || board.getBoardid().equals("1")) {
			mav.addObject("boardName", "공지사항");
		}else if(board.getBoardid().equals("2")){
			mav.addObject("boardName", "자유게시판");
		}else if(board.getBoardid().equals("3")) {
			mav.addObject("boardName", "Q&A");
		}
		List<Comment> commlist = service.commentlist(comm.getNum());
		comm.setNum(comm.getNum());
		mav.addObject("board",board);
		mav.addObject("commlist",commlist);
		mav.addObject(comm);
		// ModelAndView mav = detail(?);
		// mav.addObject(comm);
		return mav;
	}
	@PostMapping("commdel")
	public String commdel(Comment comm) {
		
		Comment dbcomm = service.commSelectOne(comm.getNum(),comm.getSeq());
		
		if(comm.getPass().equals(dbcomm.getPass())) {
			service.commdel(comm.getNum(),comm.getSeq());
		}else {
			throw new ShopException("댓글삭제 실패", "detail?num="+comm.getNum()+"#comment");
		}
		
		return "redirect:detail?num="+comm.getNum()+"#comment";
	}
}