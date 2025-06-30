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
    @Temporal(TemporalType.TIMESTAMP) // 날짜 + 시간 
    private Date saledate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userid", insertable = false, updatable = false)
    private User user;

    @OneToMany(mappedBy = "sale", fetch = FetchType.EAGER)
    private List<SaleItem> itemList = new ArrayList<>();

    @PrePersist
    public void onePersist() { // save() 함수 호출 직전에 먼저 호출
        this.saledate = new Date();
    }
    public int getTotal() {
        return itemList.stream()
                .mapToInt(s -> s.getItem().getPrice() * s.getQuantity())
                .sum();
    }
}