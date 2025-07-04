package boot_react.entity;

import boot_react.dto.BoardDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
    public BoardEntity(BoardDto boardDto) {
        this.num = boardDto.getNum();
        this.name = boardDto.getName();
        this.pass = boardDto.getPass();
        this.subject = boardDto.getSubject();
        this.content = boardDto.getContent();
        this.file1 = boardDto.getFile1();
        this.readcnt = boardDto.getReadcnt();
        this.boardid = boardDto.getBoardid();
        this.regdate = boardDto.getRegdate();
    }
}
