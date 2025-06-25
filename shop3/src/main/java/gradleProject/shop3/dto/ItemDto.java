package gradleProject.shop3.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Entity
@Table(name="item")
@Data
public class ItemDto {
	@Id
	private int id;

	@NotEmpty(message = "상품명을 입력하세요")
	private String name;
	@Min(value = 10,message = "10원이상 가능합니다")
	@Max(value = 100000, message = "10만원이하 가능합니다")
	private int price;
	@NotEmpty(message = "상품설명을 입력하세요")
	private String description;
	private String pictureUrl;
	@Transient // 컬럼과 무관한 프로퍼티
	private MultipartFile picture; // 업로드 된 파일 저장
	
	// 장바구니 비교를 위해 id만 기준으로 equals/hashCode 오버라이드
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemDto)) return false;
        ItemDto item = (ItemDto) o;
        return id == item.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
	
}
