package com.swlc.swlcexportmarketingservice.controller;

import com.swlc.swlcexportmarketingservice.dto.TestimonialDto;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.service.impl.TestimonialServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/testimonial")
public class TestimonialController {

    @Autowired
    private TestimonialServiceImpl testimonialServiceImpl;

    @PostMapping("/create")
    public ResponseEntity<CommonResponseDTO> createUser(@RequestBody TestimonialDto testimonialDto) {
        return testimonialServiceImpl.createTestimonial(testimonialDto);
    }
}
