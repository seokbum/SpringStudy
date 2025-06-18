package kr.gdu.logic;

import java.util.Date;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Board {
	private int num;
	private String boardid;
	
	@NotEmpty(message="글쓴이입력 바람")
	private String writer;
	
	@NotEmpty(message="비밀번호입력 바람")
	private String pass;
	
	@NotEmpty(message="제목입력 바람")
	private String title;
	
	@NotEmpty(message="내용입력 바람")
	private String content;
	
	private MultipartFile file1;
	private String fileurl;
	private Date regdate;
	private int readcnt;
	private int grp;
	private int grplevel;
	private int grpstep;
}
