package gradleProject.shop3.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class SaleDto {
    private int saleid;
    private String userid;
    private Date saledate;
    private UserDto user;
    private List<SaleItemDto> itemList = new ArrayList<>();

    public int getTotal() {
        return itemList.stream()
                .mapToInt(s -> s.getItem().getPrice() * s.getQuantity())
                .sum();
    }
}