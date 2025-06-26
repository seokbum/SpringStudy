package gradleProject.shop3.dto;

import gradleProject.shop3.domain.SaleItem;
import gradleProject.shop3.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
public class SaleDto {// 주문정보

    @Id
    private int saleid;
    private String userid;
    private Date saledate;
    @Transient
    private UserDto user;
    @Transient
    private List<SaleItemDto> itemList = new ArrayList<>();

    public int getTotal() {
        return itemList.stream()
                .mapToInt(s -> s.getItem().getPrice() * s.getQuantity())
                .sum();
    }
}