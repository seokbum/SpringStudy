package gradleProject.shop3.repository;


import gradleProject.shop3.domain.SaleItem;
import gradleProject.shop3.domain.SaleItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SaleItemRepository extends JpaRepository<SaleItem, SaleItemId> {


    List<SaleItem> findBySaleid(int saleid);

    @Query("SELECT COALESCE(MAX(s.saleid), 0) FROM Sale s")
    int getMaxSaleId();
}