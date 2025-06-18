package kr.gdu.dao;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.gdu.dao.mapper.ItemMapper;
import kr.gdu.logic.Item;


@Repository
public class ItemDao {

	@Autowired //SqlSessionTemplate주입 (DBConfig에서 @bean으로 등록 돼 있음)
	private SqlSessionTemplate template;
	
	private Map<String,Object> param = new HashMap<>();
	private final Class<ItemMapper> cls = ItemMapper.class;
	
	public List<Item> list() {
		//param.put("id", 2);
		return template.getMapper(cls).select(null);
	}

	public Item getItem(Integer id) {
		param.clear();
		param.put("id", id);
		return template.getMapper(cls).select(param).get(0);
	}

	public int maxId() {
		return template.getMapper(cls).maxId();
	}

	public void insert(Item item) {
		template.getMapper(cls).insert(item);
		
	}

	public void update(Item item) {
		template.getMapper(cls).update(item);
		
	}

	public void deleteItem(Integer id) {
		template.getMapper(cls).deleteItem(id);
		
	}



}
