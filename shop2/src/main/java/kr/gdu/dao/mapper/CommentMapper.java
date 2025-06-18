package kr.gdu.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import kr.gdu.logic.Comment;

@Mapper
public interface CommentMapper {

	@Select("select * from comment  where num=#{num} order by seq desc")
	List<Comment> commentList(int num);

	@Select("select ifnull(max(seq),0) from comment where num=#{val}")
	int comMaxSeq(int num);

	@Insert("insert into comment (num,seq,writer,pass,content,regdate) "
			+ " values (#{num},#{seq},#{writer},#{pass},#{content},now())")
	void comInsert(Comment comm);

	@Select("select * from comment where num=#{num} and seq=#{seq}")
	Comment commSelectOne(Map<String, Object> param);

	@Delete("delete from comment where num=#{num} and seq=#{seq}")
	void commdel(@Param("num")int num , @Param("seq") int seq);

}
