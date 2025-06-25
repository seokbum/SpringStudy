package gradleProject.shop3.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sale")
@Getter
@Setter
@ToString
public class Sale {// 주문정보
	@Id
	private int saleid;
	private String userid;
	private Date saledate;
	@Transient
	private User user;
	private List<SaleItem> itemList = new ArrayList<>();
	
	public int getTotal() {
		return itemList.stream()
				.mapToInt(s->s.getItem().getPrice() * s.getQuantity())
				.sum();
	}
}
