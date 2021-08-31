package com.swlc.swlcexportmarketingservice.repository;

import com.swlc.swlcexportmarketingservice.entity.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TestimonialRepository extends JpaRepository<Testimonial,Integer> {

    @Query(nativeQuery = true,value = "SELECT * FROM TESTIMONIAL WHERE ACTIVE=1 ORDER BY ID DESC")
    List<Testimonial> findAll();

    @Query(nativeQuery = true,value = "SELECT * FROM TESTIMONIAL t WHERE t.ACTIVE=1 AND t.CUSTOMER_NAME LIKE 1?% ORDER BY ID DESC")
    List<Testimonial> findAllWithSearch(String search);

    Testimonial findTestimonialByIdAndActiveTrue(int id);
}
