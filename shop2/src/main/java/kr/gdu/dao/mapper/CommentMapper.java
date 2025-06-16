package kr.gdu.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


import kr.gdu.logic.Comment;

@Mapper
public interface CommentMapper {

	@Select("select * from comment "
			+ "where num = #{num}")
	List<Comment> list(Integer num);

	@Select("select ifnull(max(seq),0) "
			+ "from comment"
			+ " where num = #{num}")
	int maxseq(int num);

	@Insert("insert into comment (num,seq,writer,pass,content,regdate)"
			+ " values (#{num},#{seq},#{writer},#{pass},#{content},now())")
	void insert(Comment comm);

	@Select("select * from comment "
			+ "where num = #{num} and seq = #{seq}")
	Comment selectOne(@Param("num") int num,@Param("seq") int seq);

	@Delete("Delete from comment "
			+ "where num = #{num} and seq = #{seq}")
	void delete(@Param("num") int num,@Param("seq") int seq);
	
}
