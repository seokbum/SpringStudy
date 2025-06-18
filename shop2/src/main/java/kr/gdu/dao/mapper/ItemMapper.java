package kr.gdu.dao.mapper;

import java.util.List;

import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.gdu.logic.Item;


@Mapper
public interface ItemMapper {

	@Select({"<script>",
		"select * from item <if test='id != null'>where id=#{id}</if> order by id",
		"</script>"})
	public List<Item> select(Map<String,Object> param);

	

	@Insert("insert into item values(#{id},#{name},#{price},#{description},#{pictureUrl})")
	public void insert(Item item);

	
	@Select("select ifnull(max(id),0) from item")
	public int maxId();



	@Update("update item set name=#{name},"
			+ " price=#{price},description=#{description},pictureUrl=#{pictureUrl}"
			+ " where id=#{id}")
	public void update(Item item);



	@Delete("delete from item where id=#{id}")
	public void deleteItem(Integer id);

	
	

}
