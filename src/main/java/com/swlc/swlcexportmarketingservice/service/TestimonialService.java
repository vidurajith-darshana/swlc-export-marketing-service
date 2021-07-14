package com.swlc.swlcexportmarketingservice.service;

import com.swlc.swlcexportmarketingservice.dto.TestimonialDto;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import org.springframework.http.ResponseEntity;

public interface TestimonialService {

    ResponseEntity<CommonResponseDTO> createTestimonial(TestimonialDto testimonialDto);
}
