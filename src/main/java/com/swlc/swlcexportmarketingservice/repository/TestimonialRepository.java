package com.swlc.swlcexportmarketingservice.repository;

import com.swlc.swlcexportmarketingservice.entity.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TestimonialRepository extends JpaRepository<Testimonial,Integer> {

    @Query(nativeQuery = true,value = "SELECT * FROM TESTIMONIAL")
    List<Testimonial> findAll();
}
