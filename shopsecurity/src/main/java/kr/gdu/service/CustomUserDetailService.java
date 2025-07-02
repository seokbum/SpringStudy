package kr.gdu.service;

import kr.gdu.dto.CustonUserDetails;
import kr.gdu.entity.UserEntity;
import kr.gdu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
    사용자의 인증 정보 조회 기능
 */

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // db 조회용

    /*
        spring security 에서 로그인시 호출하는 메서드
        username : 입력한 userid 값
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userData = userRepository.findByUsername(username);
        if (userData != null) {
            return new CustonUserDetails(userData); // 사용자의 정보 제공
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

}
