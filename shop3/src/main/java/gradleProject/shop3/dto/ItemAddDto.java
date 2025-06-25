package gradleProject.shop3.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ItemAddDto {
    private int id;
    private int quantity;
}