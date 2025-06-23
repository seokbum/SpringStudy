package kr.gdu.logic;



import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
/*
 * 영속객체는 JPA에서 엔티티 매니저에 의해 관리되며, 
 * 데이터베이스와 동기화되는 객체다.
 */

@Entity //영속객체로지정
@Table(name="item")//테이블명 = item (없다면  DB에만들어버림)
@Data
public class Item { 
	@Id //id컬럼이 기본키임을 지정
	private int id;
	
	@NotEmpty(message="상품명 입력")
	private String name;
	
	@Min(value=10,message="10원 이상 가능합니다")
	@Max(value=100000,message="10만원 이하만 가능합니다")
	private int price;
	
	@NotEmpty(message="상품설명 입력하세요")
	private String description;
	
	private String pictureUrl;
	//다음과같이 낙타표기법을 사용하면 picture_url 로 컬럼이만들어짐
	
	@Transient //DB에 반영되지않는다.(컬럼과 무관)
	private MultipartFile picture;
}
