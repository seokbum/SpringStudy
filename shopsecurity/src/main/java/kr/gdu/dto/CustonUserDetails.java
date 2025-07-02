package kr.gdu.dto;

import kr.gdu.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/*
    UserDetails 객체 : 사용자 정보 저장하는 클래스
        UserEntity객체가 사용자 정보 저장 객체.
        사용자 인증 처리를 위한 객체
        => 내부적으로 사용자의 인증상태를 판단하기 위한 기능
 */
public class CustonUserDetails implements UserDetails {

    // 사용자 정보를 저장하는 엔티티객체. DB 사용자 정보
    private UserEntity userEntity;

    public CustonUserDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    /*
        GrantedAuthority : 권한담당 객체
     */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userEntity.getRole();// 권한 부여
            }
        });

        return collection;
    }

    // 사용자의 암호화된 비밀번호를 반환.
    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    // 사용자ID 반환.
    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() { // 계정 만료 여부.
        return UserDetails.super.isAccountNonExpired();// 만료안됨, false : 만료됨(로그인 불가)
    }

    @Override
    public boolean isAccountNonLocked() { // 계정 잠김 여부
        return UserDetails.super.isAccountNonLocked();// 정상, false: 잠겨있음(로그인 불가)
    }

    @Override
    public boolean isCredentialsNonExpired() { // 비밀번호 만료 여부.
        return UserDetails.super.isCredentialsNonExpired();// 정상, false: 비밀번호가 만료됨(로그인 불가)
    }

    @Override
    public boolean isEnabled() { // 계정 활성화 여부
        return UserDetails.super.isEnabled();// 정상, false : 비활성화(로그인 불가)
    }
}
