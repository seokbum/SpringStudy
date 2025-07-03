package boot_react.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;

    private String name;
    private String pass;
    private String subject;
    private String content;
    private String file1;

    @Temporal(TemporalType.TIMESTAMP)
    private Date regdate;

    private int readcnt;
    private String boardid;

    @PrePersist
    public void onPrePersist() {
        this.regdate = new Date();
    }
}
