package com.swlc.swlcexportmarketingservice.service;

import com.swlc.swlcexportmarketingservice.dto.CustomerReviewSaveRequestDTO;
import com.swlc.swlcexportmarketingservice.dto.CustomerReviewsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerReviewService {
    boolean saveNewReview(CustomerReviewSaveRequestDTO dto);
    Page<CustomerReviewsDto> getCustomerReviews(int year, int month, Pageable pageable);
}
