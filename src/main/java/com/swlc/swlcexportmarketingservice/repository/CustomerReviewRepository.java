package com.swlc.swlcexportmarketingservice.repository;

import com.swlc.swlcexportmarketingservice.entity.CustomerReviews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

/**
 * @author hp
 */
public interface CustomerReviewRepository extends JpaRepository<CustomerReviews,Integer> {
    @Query("SELECT r FROM CustomerReviews r WHERE (:sDate IS NULL OR r.customerReviewDate BETWEEN :sDate AND :eDate)")
    Page<CustomerReviews> filterCustomerReviewsByDate(@Param("sDate") Date sDate, @Param("eDate") Date eDate, Pageable pageable);
}
