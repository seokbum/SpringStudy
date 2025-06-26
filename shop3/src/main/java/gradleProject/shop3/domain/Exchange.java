package gradleProject.shop3.domain;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Exchange {
    private int eno;
    private String code;
    private String name;
    private float sellamt;
    private float buyamt;
    private float priamt;
    private String edate;
}
