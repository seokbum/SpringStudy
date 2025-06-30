package gradleProject.shop3.repository;

import gradleProject.shop3.domain.Comment;
import gradleProject.shop3.domain.CommentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CommRepository extends JpaRepository<Comment, CommentId> {

    List<Comment> findByNum(int num); // JPA에서 자동으로 SQL구문 생성

    @Query("SELECT COALESCE(MAX(c.seq), 0) FROM Comment c WHERE c.num = :num")
    int maxSeq(@Param("num") int num);

}