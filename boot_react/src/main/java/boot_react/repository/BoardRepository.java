package boot_react.repository;

import boot_react.entity.BoardEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Integer>, JpaSpecificationExecutor<BoardEntity> {

    @Transactional
    @Modifying
    @Query("update BoardEntity  b set b.readcnt = b.readcnt+1 " +
            "where  b.num = :num")
    void addReadcnt(@Param("num") int num);
}
