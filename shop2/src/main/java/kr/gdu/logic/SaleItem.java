package kr.gdu.logic;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SaleItem {
	
	private int saleid; //주문번호
	private int seq; //주문상품번호
	private int itemid; //상품아이디
	private Item item;//상품아이디에 해당하는 상품정보
	private int quantity;//수량
	
	public SaleItem(int saleid , int seq , ItemSet itemSet) {
		this.saleid = saleid;
		this.seq = seq;
		this.item = itemSet.getItem();
		this.itemid = itemSet.getItem().getId();//상품id
		this.quantity = itemSet.getQuantity(); //주문수량
	}

}
