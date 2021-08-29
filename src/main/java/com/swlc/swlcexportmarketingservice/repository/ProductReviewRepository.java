package com.swlc.swlcexportmarketingservice.repository;

import com.swlc.swlcexportmarketingservice.entity.Product;
import com.swlc.swlcexportmarketingservice.entity.ProductReviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author hp
 */
public interface ProductReviewRepository extends JpaRepository<ProductReviews, Long> {
    @Query(name = "SELECT * FROM PRODUCT_REVIEWS pr WHERE pr.FK_USER = ?1 AND pr.FK_PRODUCT = ?2", nativeQuery = true)
    Optional<ProductReviews> getProductReviewsByUserAndProduct(int userId, int productId);

    @Query("SELECT COUNT(pr) FROM ProductReviews pr WHERE pr.product = :product")
    int calProductLikeCount(@Param("product") Product product);
}
