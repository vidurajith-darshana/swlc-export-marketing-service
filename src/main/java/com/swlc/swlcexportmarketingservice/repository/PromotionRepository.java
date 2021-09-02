package com.swlc.swlcexportmarketingservice.repository;

import com.swlc.swlcexportmarketingservice.entity.Promotion;
import com.swlc.swlcexportmarketingservice.enums.PromotionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion,Integer> {

    @Query("SELECT p FROM Promotion p WHERE p.heading LIKE :searchValue% AND p.status<>:pstatus GROUP BY p.id ORDER BY p.id DESC ")
    Page<Promotion> getAllPromotions(@Param("searchValue") String search, @Param("pstatus") PromotionStatus status, Pageable pageable);
}
