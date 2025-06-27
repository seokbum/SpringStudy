package gradleProject.shop3.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
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
    private Date regdate;

}
