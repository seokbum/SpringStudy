package kr.gdu.logic;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;


@Data //getter,setter,toString, EqualsAndHash
public class Item {
	
	private int id;
	//NotEmpty : null or 공백인경우 오류로인식
	@NotEmpty(message="상품명 입력")
	private String name;
	
	@Min(value=10,message="10원 이상 가능합니다")
	@Max(value=100000,message="10만원 이하만 가능합니다")
	private int price;
	
	@NotEmpty(message="상품설명 입력하세요")
	private String description;
	
	private String pictureUrl;
	private MultipartFile picture;

}
