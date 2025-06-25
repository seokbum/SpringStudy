package gradleProject.shop3.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table("SaleItem")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class SaleItem {// 주문상품
	@Id
	private int saleid;
	private int seq;
	private int itemid;
	private int quantity;
	@Transient
	private Item item;
	
	public SaleItem(int saleid, int seq, ItemSet itemSet) {
		this.saleid = saleid;
		this.seq = seq;
		this.item = itemSet.getItem();
		this.itemid = itemSet.getItem().getId();
		this.quantity = itemSet.getQuantity();
	}
}
