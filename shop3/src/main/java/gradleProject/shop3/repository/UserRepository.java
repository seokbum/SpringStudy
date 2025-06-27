package gradleProject.shop3.repository;

import gradleProject.shop3.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List; // List 임포트

public interface UserRepository extends JpaRepository<User, String> {

    @Transactional
    @Modifying
    @Query("UPDATE Usercipher u SET u.password = :chgpass WHERE u.userid = :userid")
    void updatePassword(@Param("chgpass") String chgpass, @Param("userid") String userid);

    List<User> findByPhoneno(String phoneno);
}