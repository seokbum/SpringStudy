package kr.gdu.springsecurity.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
// 새로 생성
@Setter
@Getter
@ToString
public class UserEntity { // userEntity 테이블명 생성

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String username;
    private String password;
    private String role;
}
