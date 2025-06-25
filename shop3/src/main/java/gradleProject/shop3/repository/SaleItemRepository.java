package gradleProject.shop3.repository;

import gradleProject.shop3.domain.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Integer> { // SaleItem의 기본 키 타입이 Integer라고 가정

    /**
     * 주문 번호(saleid)를 기준으로 해당 주문에 속한 모든 주문 상품(SaleItem) 목록을 조회합니다.
     * 기존 SaleRepository의 FindById(int saleid) 메소드를 대체합니다.
     */
    List<SaleItem> findBySaleid(int saleid);
}