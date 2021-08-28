package com.swlc.swlcexportmarketingservice.controller;

import com.swlc.swlcexportmarketingservice.dto.TestimonialDto;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.service.impl.TestimonialServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/testimonial")
public class TestimonialController {

    @Autowired
    private TestimonialServiceImpl testimonialServiceImpl;

    @PostMapping("/create")
    public ResponseEntity<CommonResponseDTO> createTestimonial(@RequestBody TestimonialDto testimonialDto) {
        return testimonialServiceImpl.createTestimonial(testimonialDto);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<CommonResponseDTO> deleteTestimonial(@PathVariable("id") int id) {
        return testimonialServiceImpl.deleteTestimonial(id);
    }

    @GetMapping("/getAll")
    public ResponseEntity<CommonResponseDTO> getTestimonials(@Param("search") String search) {
        return testimonialServiceImpl.getTestimonials(search);
    }
}
