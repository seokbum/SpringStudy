package gradleProject.shop3.repository;

import gradleProject.shop3.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Integer>, JpaSpecificationExecutor<Board> {

    @Query("SELECT COALESCE(MAX(b.num), 0) FROM Board b")
    int maxNum();

    @Modifying
    @Transactional
    @Query("UPDATE Board b SET b.readcnt = b.readcnt + 1 WHERE b.num = :num")
    void addReadcnt(@Param("num") int num);

    @Modifying
    @Transactional
    @Query("UPDATE Board b SET b.grpstep = b.grpstep + 1 WHERE b.grp = :grp AND b.grpstep > :grpstep")
    void grpStepAdd(@Param("grp") int grp, @Param("grpstep") int grpstep);
}