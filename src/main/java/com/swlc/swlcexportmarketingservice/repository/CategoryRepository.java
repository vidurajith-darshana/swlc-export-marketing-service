package com.swlc.swlcexportmarketingservice.repository;

import com.swlc.swlcexportmarketingservice.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category,Integer> {

    @Query("SELECT c FROM Category c GROUP BY c.id ORDER BY c.id DESC ")
    Page<Category> getAllCategories(Pageable pageable);

    @Query(value = "SELECT * FROM CATEGORY c WHERE c.NAME LIKE ?1% GROUP BY c.ID ORDER BY c.ID DESC ", nativeQuery = true)
    Page<Category> getAllCategoriesWithSearch(String search, Pageable pageable);

    @Query("SELECT c FROM Category c WHERE c.categoryStatus<>'INACTIVE' GROUP BY c.id ORDER BY c.id DESC ")
    Page<Category> getAllCategoriesByCustomer(Pageable pageable);

    @Query("SELECT COUNT(c) FROM Category c")
    int getCountOfCategories();

    Category findCategoryById(int id);

    Category findCategoryByName(String name);
}
