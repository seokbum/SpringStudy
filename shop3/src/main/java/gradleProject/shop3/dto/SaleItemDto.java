package gradleProject.shop3.dto;

import gradleProject.shop3.domain.Item;
import gradleProject.shop3.domain.ItemSet;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class SaleItemDto {// 주문상품

    @Id
    private int saleid;
    private int seq;
    private int itemid;
    private int quantity;
    @Transient
    private Item item;

    public SaleItemDto(int saleid, int seq, ItemSetDto itemSet) {
        this.saleid = saleid;
        this.seq = seq;
        this.item = itemSet.getItem();
        this.itemid = itemSet.getItem().getId();
        this.quantity = itemSet.getQuantity();
    }
}