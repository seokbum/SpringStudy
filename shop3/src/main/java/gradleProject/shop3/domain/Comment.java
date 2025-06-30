package gradleProject.shop3.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "comment")
@IdClass(CommentId.class)
@Data
@NoArgsConstructor
public class Comment {
    @Id
    private int num;
    @Id
    private int seq;
    private String writer;
    private String pass;
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    private Date regdate; // 등록일자 현재시간으로 등록

    @PrePersist
    public void onPrePersist() {
        this.regdate = new Date();
    }
}
