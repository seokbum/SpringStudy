package gradleProject.shop3.dto;

import gradleProject.shop3.domain.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SaleItemDto {

    private int saleid;
    private int seq;
    private int itemid;
    private int quantity;
    private int price;

    private Item item;

}