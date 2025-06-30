package gradleProject.shop3.repository;


import gradleProject.shop3.domain.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Integer> {



    List<Sale> findByUserid(String userid);

    @Query("select s from Sale s where s.userid=:userid")
    List<Sale> saleList(String userid);

    @Query("SELECT coalesce(max(s.saleid), 0) FROM Sale s")
    int getMaxSaleId();


}