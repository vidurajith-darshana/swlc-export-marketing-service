package com.swlc.swlcexportmarketingservice.repository;

import com.swlc.swlcexportmarketingservice.dto.row_data.Top10ProductsRowDataDTO;
import com.swlc.swlcexportmarketingservice.entity.Order;
import com.swlc.swlcexportmarketingservice.entity.Product;
import com.swlc.swlcexportmarketingservice.enums.CategoryStatus;
import com.swlc.swlcexportmarketingservice.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {

    @Query("SELECT p FROM Product p GROUP BY p.id ORDER BY p.id DESC ")
    Page<Product> getAllProducts(Pageable pageable);

    @Query(value = "SELECT * FROM PRODUCT p WHERE p.NAME LIKE ?1% OR p.CODE LIKE ?1% GROUP BY p.ID ORDER BY p.ID DESC ", nativeQuery = true)
    Page<Product> getAllProductsWithSearch(String search, Pageable pageable);

    Product findProductById(int id);

    @Query("SELECT p FROM Product p, Category c, ProductCategory pc WHERE pc.fkProduct=p AND pc.fkCategory=c AND c.categoryStatus=:catStatus AND p.status=:proStatus ORDER BY p.id DESC")
    Page<Product> getAllActiveProducts(@Param("catStatus") CategoryStatus categoryStatus, @Param("proStatus") ProductStatus productStatus, Pageable pageable);


    @Query(value = "SELECT od.FK_PRODUCT AS Pid, sum(od.QTY) AS Qty FROM ORDER_DETAIL od WHERE (?1 = 0 OR YEAR(od.CREATE_DATE) = ?1) AND (?2 = 0 OR MONTH(od.CREATE_DATE) = ?2) group by od.FK_PRODUCT order by sum(od.QTY) DESC LIMIT 10", nativeQuery = true)
    List<Top10ProductsRowDataDTO> getTop10ProductsByYearAndMonth(int yr, int mth);

    @Query("SELECT COUNT(p) FROM Product p")
    int getCountOfProducts();

}
