package com.swlc.swlcexportmarketingservice.repository;

import com.swlc.swlcexportmarketingservice.entity.NumberGenerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NumberGeneratorRepository extends JpaRepository<NumberGenerator,Integer> {

    @Query(nativeQuery = true,value = "SELECT * FROM NUMBER_GENERATOR ORDER BY ID DESC LIMIT 1")
    NumberGenerator findLastNumber();
}
