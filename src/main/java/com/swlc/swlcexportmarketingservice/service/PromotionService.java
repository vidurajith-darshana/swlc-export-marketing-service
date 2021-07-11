package com.swlc.swlcexportmarketingservice.service;

import com.swlc.swlcexportmarketingservice.dto.PromotionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface PromotionService {

    PromotionDTO savePromotion(PromotionDTO promotionDTO);

    PromotionDTO updatePromotion(PromotionDTO promotionDTO);

    Page<PromotionDTO> getAllPromotions(Pageable pageable);

    void deletePromotion(int promotionId);


}
