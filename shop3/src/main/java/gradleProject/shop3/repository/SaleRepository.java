package gradleProject.shop3.repository;

import gradleProject.shop3.domain.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {

    @Query("select s from Sale s where s.userid=:userid")
    List<Sale> saleList(String userid);

    @Query("SELECT coalesce(max(s.saleid), 0) FROM Sale s")
    int getMaxSaleId();
}