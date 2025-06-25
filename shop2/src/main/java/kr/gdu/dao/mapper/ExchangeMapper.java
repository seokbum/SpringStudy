package kr.gdu.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import kr.gdu.logic.Exchange;

@Mapper
public interface ExchangeMapper {

	@Insert("insert into exchange (code,name,priamt,sellamt,buyamt,edate) values "
			+ "(#{code},#{name},#{priamt},#{sellamt},#{buyamt},#{edate})")
	void insert(Exchange ex);

}
