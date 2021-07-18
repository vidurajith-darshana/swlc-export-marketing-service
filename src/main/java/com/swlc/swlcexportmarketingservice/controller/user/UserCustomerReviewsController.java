package com.swlc.swlcexportmarketingservice.controller.user;

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
    public ResponseEntity<CommonResponseDTO> getAllProducts(@RequestBody CustomerReviewsDto dto, HttpServletRequest httpServletRequest) {
        boolean b = customerReviewService.saveNewReview(dto);
        return new ResponseEntity<>(new CommonResponseDTO(true, "Customer reviews found successfully", null), HttpStatus.OK);
    }

}
