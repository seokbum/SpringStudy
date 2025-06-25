package gradleProject.shop3.repository;

import gradleProject.shop3.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // @Componet + DAO 관련기능. 객체화
public interface ItemRepository extends JpaRepository<Item, Integer> {

	// 이 메소드는 JpaRepository에 이미 존재하므로 중복 선언이지만, 에러를 유발하지는 않습니다.
	List<Item> findAll();

	@Query("select coalesce(max(i.id),0) from Item i")
	int findmaxId();

}