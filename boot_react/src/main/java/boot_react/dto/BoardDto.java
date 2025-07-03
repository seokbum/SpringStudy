package boot_react.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
public class BoardDto {
    private int num;
    private String name;
    private String pass;
    private String subject;
    private String content;
    private String file1;
    private Date regdate;
    private int readcnt;
    private String boardid;
}
