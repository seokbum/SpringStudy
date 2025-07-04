package logic;

import java.util.ArrayList;
import java.util.List;

public class Cart {
	
	private List<ItemSet> itemSetList = new ArrayList<>();
	
	public List<ItemSet> getItemSetList() {
		return itemSetList;
	}
	
	public void push(ItemSet itemSet) {
		// itemSet : 추가될 item
		int count = itemSet.getQuantity(); // 추가될 수량
		for(ItemSet old : itemSetList) {
			//old : 추가되어 있는 ItemSet 객체
			if(itemSet.getItem().getId() == old.getItem().getId()) {
				count = old.getQuantity() + itemSet.getQuantity();
				old.setQuantity(count);
				return;
			}
		}
		itemSetList.add(itemSet);
	}
	
	public int getTotal() { // get 프로퍼티 : total 프로퍼티
		return itemSetList.stream()
				.mapToInt(s->s.getItem().getPrice() * s.getQuantity())
				.sum();
	}
}
