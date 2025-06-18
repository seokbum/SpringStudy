package kr.gdu.dao;

import java.util.List;


import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.gdu.dao.mapper.SaleMapper;
import kr.gdu.logic.Sale;

@Repository
public class SaleDao {
	
	@Autowired
	private SqlSessionTemplate template;
	Class<SaleMapper> cls = SaleMapper.class;

	public void insert(Sale sale) {
		template.getMapper(cls).insert(sale);
	}

	public int getMaxSaleId() {
		return template.getMapper(cls).maxid();
	}

	public List<Sale> saleList(String userid) {
		return template.getMapper(cls).saleList(userid);
	}

}
