package com.swlc.swlcexportmarketingservice.controller.admin;

import com.swlc.swlcexportmarketingservice.dto.ProductDTO;
import com.swlc.swlcexportmarketingservice.dto.PromotionDTO;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
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
@RequestMapping("/api/v1/admin/promotion")
public class AdminPromotionController {

    private final PromotionService promotionService;

    public AdminPromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping("/all")
    public ResponseEntity<CommonResponseDTO> getAllPromotion(Pageable pageable, HttpServletRequest httpServletRequest) {

        log.info("End point: " + httpServletRequest.getPathInfo());

        log.info("Page : {}",pageable);

        Page<PromotionDTO> allPromotions = promotionService.getAllPromotions(pageable);

        return ResponseEntity.ok(new CommonResponseDTO(true, allPromotions));

    }

    @PostMapping("/create")
    public ResponseEntity<CommonResponseDTO> createPromotion(@RequestBody PromotionDTO promotionDTO, HttpServletRequest httpServletRequest) {

        log.info("End point: " + httpServletRequest.getPathInfo());

        promotionDTO = promotionService.savePromotion(promotionDTO);

        return ResponseEntity.ok(new CommonResponseDTO(true, promotionDTO));
    }

    @PutMapping("/update")
    public ResponseEntity<CommonResponseDTO> updateCategory(@RequestBody PromotionDTO promotionDTO, HttpServletRequest httpServletRequest) {

        log.info("End point: " + httpServletRequest.getPathInfo());

        promotionDTO = promotionService.updatePromotion(promotionDTO);

        return ResponseEntity.ok(new CommonResponseDTO(true, promotionDTO));
    }

    @DeleteMapping("/delete/{promotionId}")
    public ResponseEntity<CommonResponseDTO> updatePrmotionStatus(@PathVariable(value = "promotionId") int promotionId, @RequestParam(value = "status") PromotionStatus status, HttpServletRequest httpServletRequest) {

        log.info("End point: " + httpServletRequest.getPathInfo());

        promotionService.updatePromotionStatus(promotionId,status);

        return ResponseEntity.ok(new CommonResponseDTO(true, "Promotion status has been successfully updated!"));
    }

    @PutMapping("/update/{promotionId}/status")
    public ResponseEntity<CommonResponseDTO> deleteCategory(@PathVariable(value = "promotionId") int promotionId, HttpServletRequest httpServletRequest) {

        log.info("End point: " + httpServletRequest.getPathInfo());

        promotionService.deletePromotion(promotionId);

        return ResponseEntity.ok(new CommonResponseDTO(true, "Promotion has been successfully deleted!"));
    }


}
