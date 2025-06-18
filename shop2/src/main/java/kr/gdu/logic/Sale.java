package kr.gdu.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Sale { //DB의 sale테이블의 내용 + 사용자정보 + 주문상품정보
	private int saleid; //주문번호
	private String userid; //고객아이디
	private Date saledate; //주문일자
	private User user; //고객의정보
	
	private List<SaleItem> itemList = new ArrayList<>();
	
	public int getTotal() {
		return itemList.stream()
				.mapToInt(s->s.getItem().getPrice() * s.getQuantity()).sum();
	}
}
