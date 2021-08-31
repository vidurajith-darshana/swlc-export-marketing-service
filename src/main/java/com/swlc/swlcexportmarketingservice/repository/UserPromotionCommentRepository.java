package com.swlc.swlcexportmarketingservice.repository;

import com.swlc.swlcexportmarketingservice.entity.Promotion;
import com.swlc.swlcexportmarketingservice.entity.UserPromotionComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author hp
 */
public interface UserPromotionCommentRepository extends JpaRepository<UserPromotionComment, Integer> {
    List<UserPromotionComment> findByFkPromotion(Promotion promotion);
}
