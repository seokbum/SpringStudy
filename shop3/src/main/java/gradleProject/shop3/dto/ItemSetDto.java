package gradleProject.shop3.dto;

import gradleProject.shop3.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class ItemSetDto {
    private Item item;
    private Integer quantity; //수량
}
