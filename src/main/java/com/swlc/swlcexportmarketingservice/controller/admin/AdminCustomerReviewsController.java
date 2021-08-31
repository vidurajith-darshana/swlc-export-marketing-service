package com.swlc.swlcexportmarketingservice.controller.admin;

import com.swlc.swlcexportmarketingservice.dto.CustomerReviewsDto;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.service.CustomerReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.swlc.swlcexportmarketingservice.constant.ApplicationConstant.APPLICATION_ERROR_OCCURRED_MESSAGE;

/**
 * @author hp
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/customer/reviews")
public class AdminCustomerReviewsController {

    private final CustomerReviewService customerReviewService;

    @Autowired
    public AdminCustomerReviewsController(CustomerReviewService customerReviewService) {
        this.customerReviewService = customerReviewService;
    }

    @GetMapping("/year/{year}/month/{month}")
    public ResponseEntity<CommonResponseDTO> getAllProducts(@PathVariable("year") int year, @PathVariable("month") int month, Pageable pageable, HttpServletRequest httpServletRequest) {
        Page<CustomerReviewsDto> customerReviews = customerReviewService.getCustomerReviews(year, month, pageable);
        return new ResponseEntity<>(new CommonResponseDTO(true, "Customer reviews found successfully", customerReviews), HttpStatus.OK);
    }
}
