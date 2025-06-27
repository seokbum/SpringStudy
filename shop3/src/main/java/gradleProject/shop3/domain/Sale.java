package gradleProject.shop3.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sale")
@Getter
@Setter
public class Sale {

    @Id
    private int saleid;
    private String userid;
    private Date saledate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userid", insertable = false, updatable = false)
    private User user;

    @OneToMany(mappedBy = "sale", fetch = FetchType.EAGER)
    private List<SaleItem> itemList = new ArrayList<>();

    public int getTotal() {
        if (itemList == null || itemList.isEmpty()) return 0;
        return itemList.stream()
                .filter(s -> s.getItem() != null)
                .mapToInt(s -> s.getItem().getPrice() * s.getQuantity())
                .sum();
    }
}