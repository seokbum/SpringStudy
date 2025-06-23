	package kr.gdu.repository;
	
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kr.gdu.logic.Item;
	
	// JpaRepository<클래스,기본자료형>의 하위인터페이스는 자동 객체 생성함
	
	@Repository
	public interface ItemRepository extends JpaRepository<Item, Integer> {
		
		//coalesce(JPA)  == ifnull(MySQL)
		@Query("select coalesce(max(i.id),0) from Item i ")
		int findMaxId();
	}
