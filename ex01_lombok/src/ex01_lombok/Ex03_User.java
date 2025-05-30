package ex01_lombok;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@Getter
@Setter
public class Ex03_User {
	private String id;
	private String pw;
}
