package gradleProject.shop3.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "saleitem")
@IdClass(SaleItemId.class)
@Getter
@Setter
@NoArgsConstructor
public class SaleItem {

    @Id
    private int saleid;
    @Id
    private int seq;

    private int itemid;
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saleid", insertable = false, updatable = false)
    private Sale sale;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "itemid", insertable = false, updatable = false)
    private Item item;

    public SaleItem(int saleid, int seq, ItemSet itemSet) {
        this.saleid = saleid;
        this.seq = seq;
        this.itemid = itemSet.getItem().getId();
        this.quantity = itemSet.getQuantity();
    }
}