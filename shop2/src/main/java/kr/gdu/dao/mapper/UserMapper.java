package kr.gdu.dao.mapper;

import java.util.List;

import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.gdu.logic.User;


@Mapper
public interface UserMapper {

	@Insert("insert into useraccount "
			
			+ " (userid,password,username,phoneno,postcode,email,birthday,address)"
			+ " values(#{userid},#{password},#{username},#{phoneno},#{postcode},"
			+ " #{email},#{birthday},#{address})")
	void insert(User user);

	@Select("select userid from useraccount where password=#{password} and userid=#{userid}")
	String selectUser(User user);
	
	@Select({"<script>",
        "SELECT * FROM useraccount ",
        "<if test='userid != null'> where userid=#{userid}</if> ",
        "<if test='userids != null'>",
        "WHERE userid IN ",
        "<foreach item='id' collection='userids' open='(' separator=',' close=')'>",
        "#{id}",
        "</foreach>",
        "</if>",
        "</script>"})
List<User> select(Map<String, Object> param);

	
	@Update("update useraccount set username=#{username}, "
			+ " birthday=#{birthday}, address=#{address}, "
			+ " phoneno=#{phoneno}, email=#{email}, "
			+ " postcode=#{postcode}"
			+ "where userid=#{userid}")
	void update(User user);

	@Delete("delete from useraccount where userid=#{userid}")
	void delete(String userid);

	@Update("update useraccount set password=#{password} where userid=#{userid}")
	void changePw(User loginUser);

	@Select({"<script>",
		"select ${col} from useraccount "
		+ "where email=#{email} and phoneno=#{phoneno} "
		+ "<if test='userid != null'> and userid=#{userid}</if> "
		+ "</script>"})
	String search(Map<String, Object> param);

	@Select("select * from useraccount")
	List<User> list();
	

}
