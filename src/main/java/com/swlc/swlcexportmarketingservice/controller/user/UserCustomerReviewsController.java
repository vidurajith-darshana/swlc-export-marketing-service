package com.swlc.swlcexportmarketingservice.controller.user;

import com.swlc.swlcexportmarketingservice.dto.CustomerReviewSaveRequestDTO;
import com.swlc.swlcexportmarketingservice.dto.CustomerReviewsDto;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.service.CustomerReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hp
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/user/customer/reviews")
public class UserCustomerReviewsController {

    private final CustomerReviewService customerReviewService;

    @Autowired
    public UserCustomerReviewsController(CustomerReviewService customerReviewService) {
        this.customerReviewService = customerReviewService;
    }

    @PostMapping
    public ResponseEntity<CommonResponseDTO> getSaveCustomerReview(@RequestBody CustomerReviewSaveRequestDTO dto, HttpServletRequest httpServletRequest) {
        boolean b = customerReviewService.saveNewReview(dto);
        return new ResponseEntity<>(new CommonResponseDTO(true, "Customer reviews saved successfully", null), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<CommonResponseDTO> getAllReview(@RequestParam("yr") int yr, @RequestParam("mnth") int mnth, Pageable pageable, HttpServletRequest httpServletRequest) {
        Page<CustomerReviewsDto> results = customerReviewService.getCustomerReviews(yr, mnth, pageable);
        return new ResponseEntity<>(new CommonResponseDTO(true, "Customer reviews found successfully", results), HttpStatus.OK);
    }

}
