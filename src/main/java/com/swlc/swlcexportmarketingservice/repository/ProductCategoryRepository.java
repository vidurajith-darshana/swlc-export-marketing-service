package com.swlc.swlcexportmarketingservice.repository;

import com.swlc.swlcexportmarketingservice.entity.Product;
import com.swlc.swlcexportmarketingservice.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Integer> {

    List<ProductCategory> findByFkProduct(Product product);

}
