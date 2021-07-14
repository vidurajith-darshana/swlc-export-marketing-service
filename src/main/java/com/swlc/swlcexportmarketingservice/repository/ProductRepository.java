package com.swlc.swlcexportmarketingservice.repository;

import com.swlc.swlcexportmarketingservice.entity.Product;
import com.swlc.swlcexportmarketingservice.enums.CategoryStatus;
import com.swlc.swlcexportmarketingservice.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product,Integer> {

    @Query("SELECT p FROM Product p GROUP BY p.id ORDER BY p.id DESC ")
    Page<Product> getAllProducts(Pageable pageable);

    @Query("SELECT p FROM Product p, Category c, ProductCategory pc WHERE pc.fkProduct=p AND pc.fkCategory=c AND c.categoryStatus=:catStatus AND p.status=:proStatus ORDER BY p.id DESC")
    Page<Product> getAllActiveProducts(@Param("catStatus") CategoryStatus categoryStatus, @Param("proStatus") ProductStatus productStatus, Pageable pageable);
}
