package kr.gdu.dao.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.RequestParam;

import kr.gdu.dto.board.BoardCountDto;
import kr.gdu.dto.board.BoardDetailDto;
import kr.gdu.dto.board.DeleteBoardDto;
import kr.gdu.logic.Board;
import kr.gdu.logic.Comment;
@Mapper
public interface BoardMapper {
    String select = "select num,writer,pass,title,content,file1 AS fileurl,"
		+ " regdate, readcnt, grp, grplevel, grpstep, boardid from board";
    
    
    @Select({"<script>",
   	"select count(*) from board where boardid=#{boardid} ",
    "<if test='searchtype != null'> "
    + " and ${searchtype} like '%${searchcontent}%'</if>",
   	"</script>"})
	int count(BoardCountDto dto);
    
    @Select({"<script>",
        select,
        "<where>", 
        "<if test='num != null'> num = #{num} </if>",
        "<if test='num == null and boardid != null'> boardid = #{boardid} </if>",
        "<if test='searchtype != null'> and ${searchtype} like '%${searchcontent}%'</if>",
        "</where>",
        "<if test='limit != null and startrow != null'> "
        + " order by grp desc, grpstep asc limit #{startrow},#{limit}</if>",
        "<if test='limit != null and startrow == null'> "
        + " order by grp desc, grpstep asc limit 0,#{limit}</if>",
        "</script>"})    
	List<Board> select(Map<String, Object> param);

    
    @Select(select+" where num=#{num}")
	Board selectOne(int num);
    
    @Update("update board set readcnt = readcnt+1 where num=#{val}")
    void addReadcnt(int num);

    @Select("select ifnull(max(num),1) from board")
	int maxNum();

    @Insert("insert into board "
    		+ "(num,boardid,writer,pass,title,content,file1, "
    		+ " regdate,readcnt,grp,grplevel,grpstep) values "
    		+ "( #{num},#{boardid},#{writer},#{pass},#{title},#{content}, "
    		+ " #{fileurl},now(),0,#{grp},#{grplevel},#{grpstep})")
	void insert(Board board);

    @Update("update board "
    		+ "set writer=#{writer},title=#{title}, "
    		+ "content=#{content},file1=#{fileurl} "
    		+ "where num=#{num}")
	void update(Board board);

    @Delete("delete from board where num=#{num}")
	void delete(DeleteBoardDto dto);

    @Update("update board set grpstep=grpstep+1 "
    		+ " where grp=#{grp} and grpstep > #{grpstep}")
	void grpStepAdd(Board board);

    @Select("select writer,count(*) cnt from board where boardid=#{val} "
    		+ " group by writer order by cnt desc limit 0,7")
    // 글 갯수 내림차순 정렬
	List<Map<String, Object>> graph1(String id);

    @Select("select date(regdate) reg ,count(*) cnt from board where boardid=#{val} "
    		+ " group by date(regdate) order by cnt desc limit 0,7")
	List<Map<Date, Object>> graph2(String id);

   
    
    
	
}