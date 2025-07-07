package kr.gdu.logic;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;




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
