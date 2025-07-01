package kr.gdu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class UserEntity { //userEntity 테이블명 생성
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) // 자동번호 생성
	private int id;
	@Column(unique=true)
	private String username; // userid. 중복불가
	private String password; // 비밀번호
	private String role; // 권한.
}
