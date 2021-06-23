package com.swlc.swlcexportmarketingservice.repository;

import com.swlc.swlcexportmarketingservice.entity.Product;
import com.swlc.swlcexportmarketingservice.entity.ProductCategory;
import com.swlc.swlcexportmarketingservice.enums.CategoryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Integer> {

    List<ProductCategory> findByFkProduct(Product product);

    @Query("SELECT pc.fkProduct FROM  ProductCategory pc WHERE pc.fkCategory.categoryStatus=:categoryStatus")
    Page<ProductCategory> getProductsByCategoryStatus(@Param("categoryStatus") CategoryStatus categoryStatus, Pageable pageable);

}
