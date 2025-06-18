package kr.gdu.Interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.gdu.exception.ShopException;
import kr.gdu.logic.User;

public class BoardInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String boardid = request.getParameter("boardid");
		HttpSession session = request.getSession();
		User login = (User)session.getAttribute("loginUser");
		if(boardid==null || boardid.equals("1")) {
			//공지사항(boardid=='1')은 관리자만 쓸수있음!!!
			if(login==null) {
				String msg = "로그인부터하세요";
				String url = request.getContextPath()+"/user/login";
				throw new ShopException(msg, url);	
			}
			else if(!login.getUserid().equals("admin")) {
				String msg = "관리자만 등록가능";
				String url = request.getContextPath()+"/board/list?boardid=1";
				throw new ShopException(msg, url);	
			}
		}
		//로그인을안하면 자유게시판도 쓸수없게막아야함
		else if(boardid.equals("2")) {
			if(login==null) {
				String msg = "로그인부터하세요";
				String url = request.getContextPath()+"/user/login";
				throw new ShopException(msg, url);	
			}
		}
		return true; //BoardCotroller 정상실행
		
	}

	
}
