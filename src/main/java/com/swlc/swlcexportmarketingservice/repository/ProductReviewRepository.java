package com.swlc.swlcexportmarketingservice.repository;

import com.swlc.swlcexportmarketingservice.entity.Product;
import com.swlc.swlcexportmarketingservice.entity.ProductReviews;
import com.swlc.swlcexportmarketingservice.entity.User;
import com.swlc.swlcexportmarketingservice.enums.ProductReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author hp
 */
public interface ProductReviewRepository extends JpaRepository<ProductReviews, Long> {
    @Query("SELECT pr FROM ProductReviews pr WHERE pr.user = :user AND pr.product = :product")
    Optional<ProductReviews> getProductReviewsByUserAndProduct(@Param("user") User user, @Param("product") Product product);

    @Query("SELECT COUNT(pr) FROM ProductReviews pr WHERE pr.product = :product AND pr.status = :likeStatus")
    int calProductLikeCount(@Param("product") Product product, @Param("likeStatus") ProductReviewStatus productReviewStatus);
}
