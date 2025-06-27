package gradleProject.shop3.service;

import gradleProject.shop3.domain.Mail;
import gradleProject.shop3.domain.User;
import gradleProject.shop3.dto.UserDto;
import gradleProject.shop3.repository.UserRepository;
import gradleProject.shop3.util.CipherUtil;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Transactional // 클래스 레벨에 적용된 기본 트랜잭션 설정
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    public void userInsert(User user) {
        userRepository.save(user);
    }

    public void userUpdate(User user) {
        userRepository.save(user);
    }

    public void userDelete(String userid) {
        userRepository.deleteById(userid);
    }

    public void updatePassword(String userid, String chgpass) {
        userRepository.updatePassword(chgpass, userid);
    }

    public User selectUser(String userid) {
        return userRepository.findById(userid).orElse(null);
    }

    public String findUserId(String email, String phoneno) {
        List<User> userList = userRepository.findByPhoneno(phoneno);
        if (userList.isEmpty()) {
            return null;
        }
        for (User user : userList) {
            String decryptedEmail = decryptEmailForUser(user).getEmail();
            if (decryptedEmail.equalsIgnoreCase(email)) {
                return user.getUserid();
            }
        }
        return null;
    }

    public boolean verifyUserForPasswordReset(String userid, String email, String phoneno) {
        User user = userRepository.findById(userid).orElse(null);
        if (user == null) {
            return false;
        }
        String decryptedEmail = decryptEmailForUser(user).getEmail();
        return user.getPhoneno().equals(phoneno) && decryptedEmail.equalsIgnoreCase(email);
    }

    public List<User> findAllAndDecrypt() {
        List<User> list = userRepository.findAll();
        list.forEach(this::decryptEmailForUser);
        return list;
    }

    public List<User> findUsersByIdsAndDecrypt(String[] idchks) {
        if (idchks == null || idchks.length == 0) return Collections.emptyList();
        List<User> list = userRepository.findAllById(Arrays.asList(idchks));
        list.forEach(this::decryptEmailForUser);
        return list;
    }

    public boolean mailSend(Mail mail) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(mail.getGoogleid() + "@gmail.com");
            helper.setTo(mail.getRecipient().split(","));
            helper.setSubject(mail.getTitle());
            helper.setText(mail.getContents(), true);

            if (mail.getFile1() != null && !mail.getFile1().isEmpty()) {
                for (MultipartFile mf : mail.getFile1()) {
                    if (!mf.isEmpty()) {
                        helper.addAttachment(mf.getOriginalFilename(), new ByteArrayResource(mf.getBytes()));
                    }
                }
            }
            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void mailfileDelete(Mail mail) {
        // 필요 시 파일 삭제 로직 구현
    }

    private User decryptEmailForUser(User user) {
        try {
            String key = CipherUtil.makehash(user.getUserid());
            String plainEmail = CipherUtil.decrypt(user.getEmail(), key);
            user.setEmail(plainEmail);
        } catch (Exception e) {
            user.setEmail("(복호화 불가)");
        }
        return user;
    }

    public UserDto encryptEmail(UserDto userDto) throws Exception {
        String cipherUserid = CipherUtil.makehash(userDto.getUserid());
        String cipherEmail = CipherUtil.encrypt(userDto.getEmail(), cipherUserid);
        userDto.setEmail(cipherEmail);

        return userDto;
    }

    public String decryptEmail(User user) {
        try{
            String hashId = CipherUtil.makehash(user.getUserid());
            System.err.println("user객체 email확인: " + user.getEmail());
            System.err.println("user객체 hashId확인: " + hashId);
            String email = CipherUtil.decrypt(user.getEmail(), hashId);
            return email;
        }
        catch (Exception e) {
            System.err.println("이메일 복호화 오류");
            e.printStackTrace();
        }
        return null;
    }

}