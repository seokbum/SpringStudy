package gradleProject.shop3.service;

import gradleProject.shop3.domain.Mail;
import gradleProject.shop3.domain.User;
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
@Transactional
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

    @Transactional(readOnly = true)
    public User selectUser(String userid) {
        return userRepository.findById(userid).orElse(null);
    }



    @Transactional(readOnly = true)
    public List<User> findAllAndDecrypt() {
        List<User> list = userRepository.findAll();
        list.forEach(this::decryptEmailForUser);
        return list;
    }

    @Transactional(readOnly = true)
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

            // 최종 메일 발송
            javaMailSender.send(message);
            return true; // 성공 시 true 반환

        } catch (Exception e) {
            e.printStackTrace();
            return false; // 실패 시 false 반환
        }
    }

    public void mailfileDelete(Mail mail) {
        // (이 메소드는 필요 시 실제 파일 삭제 로직 구현)
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

    public String getSearch(User user) throws Exception {

        if (user.getUserid() == null) { //id찾기겠지
            String email = user.getEmail();
            String phoneno = user.getPhoneno();

            List<User> users = userRepository.searchByUserid(phoneno);
            for (User u : users) {
                u = CipherUtil.emailDecrypt(u);
                if (u.getEmail().equalsIgnoreCase(email)) {
                    return u.getUserid();
                }
            }
       }
//      else {
//            User user1 = CipherUtil.emailEncrypt(user);
////            return userRepository.searchByPassword(user1.getUserid(), user1.getEmail(), user1.getPhoneno());
//        }
        return null;

    }
}