package com.swlc.swlcexportmarketingservice.service;

import com.swlc.swlcexportmarketingservice.dto.PromotionDTO;
import com.swlc.swlcexportmarketingservice.enums.PromotionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface PromotionService {

    PromotionDTO savePromotion(PromotionDTO promotionDTO);

    PromotionDTO updatePromotion(PromotionDTO promotionDTO);

    Page<PromotionDTO> getAllPromotions(Pageable pageable);

    void deletePromotion(int promotionId);

    void updatePromotionStatus(int promotionId, PromotionStatus status);


}
