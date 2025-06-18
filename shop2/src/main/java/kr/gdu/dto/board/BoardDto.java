package kr.gdu.dto.board;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class BoardDto {
	private int num;
	private String boardid;
	private String writer;
	private String pass;
	private String title;
	private String content;
	private MultipartFile file1;
	private String fileurl;
	private Date regdate;
	private int readcnt;
	private int grp;
	private int grplevel;
	private int grpstep;
	private int startrow;
	int pageNum;
	int limit;
	String searchtype;
	String searchcontent;
	
	public int getStartrow() {
		return (this.pageNum - 1) * this.limit;
	}
	
}
