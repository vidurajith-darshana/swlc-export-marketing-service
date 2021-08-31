package com.swlc.swlcexportmarketingservice.controller.user;

import com.swlc.swlcexportmarketingservice.dto.PromotionDTO;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.dto.response.PromotionUserResponseDTO;
import com.swlc.swlcexportmarketingservice.entity.Promotion;
import com.swlc.swlcexportmarketingservice.enums.PromotionLikeStatus;
import com.swlc.swlcexportmarketingservice.enums.PromotionStatus;
import com.swlc.swlcexportmarketingservice.service.PromotionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/v1/user/promotion")
public class UserPromotionController {

    private final PromotionService promotionService;

    public UserPromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping("/all")
    public ResponseEntity<CommonResponseDTO> getAllPromotion(Pageable pageable, HttpServletRequest httpServletRequest) {

        log.info("End point: " + httpServletRequest.getPathInfo());

        log.info("Page : {}",pageable);

        Page<PromotionUserResponseDTO> allPromotions = promotionService.getAllPromotions(pageable);

        return ResponseEntity.ok(new CommonResponseDTO(true, allPromotions));

    }

    @PatchMapping("/like")
    public ResponseEntity<CommonResponseDTO> likeToPromotion(@RequestParam("promotion") int id, @RequestParam("status") PromotionLikeStatus promotionStatus) {
        promotionService.likePromotion(id, promotionStatus);
        return ResponseEntity.ok(new CommonResponseDTO(true, "Your action proceed successfully!"));
    }

    @PatchMapping("/comment")
    public ResponseEntity<CommonResponseDTO> likeToPromotion(@RequestParam("promotion") int id, @RequestParam("comment") String comment) {
        promotionService.commentOnPromotion(id, comment);
        return ResponseEntity.ok(new CommonResponseDTO(true, "Your comment posted successfully!"));
    }
}
