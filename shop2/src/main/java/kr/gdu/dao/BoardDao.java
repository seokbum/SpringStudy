package kr.gdu.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.gdu.dao.mapper.BoardMapper;
import kr.gdu.dto.board.BoardCountDto;
import kr.gdu.dto.board.BoardDetailDto;
import kr.gdu.dto.board.DeleteBoardDto;
import kr.gdu.logic.Board;
import kr.gdu.logic.Comment;

@Repository
public class BoardDao {
	
	@Autowired
	private SqlSessionTemplate template;
	private Class<BoardMapper> cls = BoardMapper.class;
	private Map<String,Object> param = new HashMap<>();
	
	public int count(BoardCountDto dto) {
		int count = template.getMapper(cls).count(dto);
		return count;
	}	
	public List<Board> list	(Integer pageNum, int limit, 
			String boardid, String searchtype, String searchcontent) {
		param.clear();
		param.put("startrow", (pageNum - 1) * limit); 
		param.put("limit",  limit);		
		param.put("boardid",  boardid);		
		param.put("searchtype",searchtype);
		param.put("searchcontent",searchcontent);
		return template.getMapper(cls).select(param);
	}
	public Board selectOne(int num) {
		return template.getMapper(cls).selectOne(num);
	}
	public void addReadcnt(int num) {
		template.getMapper(cls).addReadcnt(num);
		
	}
	public int maxNum() {
		return template.getMapper(cls).maxNum();
	}
	
	public void insert(Board board) {
		 template.getMapper(cls).insert(board);
	}
	
	public void update(Board board) {
		template.getMapper(cls).update(board);
		
	}
	public void delete(DeleteBoardDto dto) {
		template.getMapper(cls).delete(dto);
		
	}
	public void grpStepAdd(Board board) {
		template.getMapper(cls).grpStepAdd(board);
		
	}
	public List<Map<String, Object>> graph1(String id) {		
		return template.getMapper(cls).graph1(id);
	}
	public List<Map<Date, Object>> graph2(String id) {
		return template.getMapper(cls).graph2(id);
	}
	
}