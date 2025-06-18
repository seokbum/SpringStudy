package kr.gdu.dto.board;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class DeleteBoardDto {
	
	private int num;
	@NotEmpty(message="싫어")
	private String pass;
	private String boardid;

}
