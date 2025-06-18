package kr.gdu.logic;

import java.util.ArrayList;
import java.util.List;

import lombok.ToString;

@ToString
public class Cart {
	private List<ItemSet> itemSetList = new ArrayList<ItemSet>();
	
	public List<ItemSet> getItemSetList(){
		return itemSetList;
	}
	
	public void push(ItemSet set) {
		
		Integer count = set.getQuantity();
		for (ItemSet old : itemSetList) {
			//old : 추가되어있는 ItemSet객체
			if(set.getItem().getId()==old.getItem().getId()) {
				count = old.getQuantity() + set.getQuantity();
				old.setQuantity(count);
				return;
			}
		}
		itemSetList.add(set);
	}
	
	public int getTotal() {
		return itemSetList.stream()
				//getItem().getPrice : item객체의 price프로퍼티
				.mapToInt(s->s.getItem().getPrice() * s.getQuantity())
				.sum();
	}
	

}
