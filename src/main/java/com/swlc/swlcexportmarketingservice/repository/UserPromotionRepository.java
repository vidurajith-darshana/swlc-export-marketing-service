package com.swlc.swlcexportmarketingservice.repository;

import com.swlc.swlcexportmarketingservice.entity.Promotion;
import com.swlc.swlcexportmarketingservice.entity.User;
import com.swlc.swlcexportmarketingservice.entity.UserPromotion;
import com.swlc.swlcexportmarketingservice.enums.ProductReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPromotionRepository extends JpaRepository<UserPromotion,Integer> {

    @Query("SELECT COUNT(u) FROM UserPromotion u WHERE u.fkPromotion = :promotion AND u.likeStatus = :likeType")
    int getUserPromotionCount(@Param("promotion")Promotion promotion, @Param("likeType") Integer likeType);

    @Query("SELECT u FROM UserPromotion u WHERE u.fkUser = :user AND u.fkPromotion = :promotion")
    Optional<UserPromotion> checkLiked(@Param("user") User user, @Param("promotion")Promotion promotion);
}
