package kr.gdu.logic;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor //모든필드를 사용한 생성자
public class ItemSet {
	private Item item;
	private Integer quantity;

}
