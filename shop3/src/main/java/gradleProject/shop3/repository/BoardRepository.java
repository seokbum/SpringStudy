package gradleProject.shop3.repository;

import gradleProject.shop3.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Integer>, JpaSpecificationExecutor<Board> {

    @Query("SELECT COALESCE(MAX(b.num), 0) FROM Board b")
    int maxNum();

    @Modifying
    @Query("UPDATE Board b SET b.readcnt = b.readcnt + 1 WHERE b.num = :num")
    void addReadcnt(@Param("num") int num);

    @Modifying
    @Query("UPDATE Board b SET b.grpstep = b.grpstep + 1 WHERE b.grp = :grp AND b.grpstep > :grpstep")
    void grpStepAdd(@Param("grp") int grp, @Param("grpstep") int grpstep);

    // 글쓴이별 작성건수
    @Query("select b.writer as writer , count(b) cnt  from Board b where b.boardid=:id " +
            " group by b.writer order by cnt DESC")
    List<Map<String, Object>> graph1(String id);

    @Query(value = "select date_format(coalesce(b.regdate, now()), '%Y-%m-%d') as day, count(*) as cnt " +
            "from Board b where b.boardid = :boardid " +
            "group by date_format(coalesce(b.regdate, now()), '%Y-%m-%d') " +
            "order by day desc limit 7", nativeQuery = true)
    List<Map<String, Object>> graph2(@Param("boardid") String boardid);
}