package gradleProject.shop3.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SaleItemId implements Serializable {
    private int saleid;
    private int seq;
}
