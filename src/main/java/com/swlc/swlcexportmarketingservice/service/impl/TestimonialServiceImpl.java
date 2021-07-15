package com.swlc.swlcexportmarketingservice.service.impl;

import com.swlc.swlcexportmarketingservice.dto.TestimonialDto;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.entity.Testimonial;
import com.swlc.swlcexportmarketingservice.exception.SwlcExportMarketException;
import com.swlc.swlcexportmarketingservice.repository.TestimonialRepository;
import com.swlc.swlcexportmarketingservice.service.TestimonialService;
import com.swlc.swlcexportmarketingservice.util.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.swlc.swlcexportmarketingservice.constant.ApplicationConstant.*;

@Service
public class TestimonialServiceImpl implements TestimonialService {

    @Autowired
    private FileHandler fileHandler;

    @Autowired
    private TestimonialRepository testimonialRepository;

    @Override
    public ResponseEntity<CommonResponseDTO> createTestimonial(TestimonialDto testimonialDto) {
        try {
            Testimonial testimonial;

            if (testimonialDto.getId() != 0) {
                testimonial = testimonialRepository.findTestimonialByIdAndActiveTrue(testimonialDto.getId());
                if (testimonial == null) throw new SwlcExportMarketException(404,NOT_FOUND_TESTIMONIAL);
            } else {
                testimonial = new Testimonial();
            }

            if ((testimonialDto.getYoutubeUrl() == null || testimonialDto.getYoutubeUrl().isEmpty()) && testimonialDto.getId() == 0) throw new SwlcExportMarketException(404,NOT_FOUND_VIDEO);

            if (testimonialDto.getYoutubeUrl() != null) testimonial.setYoutubeUrl(testimonialDto.getYoutubeUrl());
            if (testimonialDto.getComment() != null) testimonial.setComment(testimonialDto.getComment());
            if (testimonialDto.getCountry() != null) testimonial.setCountry(testimonialDto.getCountry());
            if (testimonialDto.getCustomerName() != null) testimonial.setCustomerName(testimonialDto.getCustomerName());

            if (testimonialDto.getImage() != null) {
                String imageUrl = fileHandler.saveImageFile(testimonialDto.getImage());
                testimonial.setImageUrl(imageUrl);
            }
            testimonial.setActive(true);

            testimonialRepository.save(testimonial);

            return new ResponseEntity<>(new CommonResponseDTO(true, REQUEST_SUCCESS_MESSAGE, testimonialDto.getId() == 0 ? "Testimonial created successfully!" : "Testimonial updated successfully!"), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<CommonResponseDTO> deleteTestimonial(int testimonialId) {
        try {
            Testimonial testimonial = testimonialRepository.findTestimonialByIdAndActiveTrue(testimonialId);
            if (testimonial == null) throw new SwlcExportMarketException(404,NOT_FOUND_TESTIMONIAL);
            testimonial.setActive(false);

            testimonialRepository.save(testimonial);

            return new ResponseEntity<>(new CommonResponseDTO(true, REQUEST_SUCCESS_MESSAGE, "Testimonial deleted successfully!"), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<CommonResponseDTO> getTestimonials() {
        try {
            List<Testimonial> testimonials = testimonialRepository.findAll();
            List<TestimonialDto> list = new ArrayList<>();
            if (testimonials != null && !testimonials.isEmpty()) {
                for (Testimonial testimonial: testimonials) {

                    TestimonialDto testimonialDto = new TestimonialDto();
                    testimonialDto.setComment(testimonial.getComment());
                    testimonialDto.setCountry(testimonial.getCountry());
                    testimonialDto.setId(testimonial.getId());
                    testimonialDto.setCustomerName(testimonial.getCustomerName());
                    testimonialDto.setImage(testimonial.getImageUrl());
                    testimonialDto.setYoutubeUrl(testimonial.getYoutubeUrl());

                    list.add(testimonialDto);
                }
            }
            return new ResponseEntity<>(new CommonResponseDTO(true, REQUEST_SUCCESS_MESSAGE, list), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
