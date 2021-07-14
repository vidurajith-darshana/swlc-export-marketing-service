package com.swlc.swlcexportmarketingservice.service.impl;

import com.swlc.swlcexportmarketingservice.dto.TestimonialDto;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.entity.Testimonial;
import com.swlc.swlcexportmarketingservice.service.TestimonialService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TestimonialServiceImpl implements TestimonialService {

    @Override
    public ResponseEntity<CommonResponseDTO> createTestimonial(TestimonialDto testimonialDto) {
        try {
            Testimonial testimonial = new Testimonial();
            testimonial.setComment(testimonialDto.getComment());
            testimonial.setCountry(testimonialDto.getCountry());
            testimonial.setCustomerName(testimonialDto.getCustomerName());
            testimonial.setYoutubeUrl(testimonialDto.getYoutubeUrl());

        } catch (Exception e) {

        }
        return null;
    }
}
