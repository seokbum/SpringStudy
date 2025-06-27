package gradleProject.shop3.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "board")
@NoArgsConstructor
public class Board {
    @Id
    private int num;
    private String boardid;
    private String writer;
    private String pass;
    private String title;
    private String content;
    private String fileurl;
    private Date regdate;
    private int readcnt;
    private int grp;
    private int grplevel;
    private int grpstep;
    private int commentCnt;
}