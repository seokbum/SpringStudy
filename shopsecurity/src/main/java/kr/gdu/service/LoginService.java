package kr.gdu.service;

import kr.gdu.dto.JoinDto;
import kr.gdu.entity.UserEntity;
import kr.gdu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(JoinDto joinDto) {
        UserEntity data = new UserEntity();
        data.setUsername(joinDto.getUsername());
        data.setPassword(bCryptPasswordEncoder.encode(joinDto.getPassword()));
        if (joinDto.getUsername().equals("admin")) {
            data.setRole("ROLE_ADMIN");
        } else {
            data.setRole("ROLE_USER");
        }
        userRepository.save(data);
    }
}
