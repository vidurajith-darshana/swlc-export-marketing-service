package com.swlc.swlcexportmarketingservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TestimonialDto {

    private int id;
    private String imageUrl;
    private String youtubeUrl;
    private String customerName;
    private String country;
    private String comment;
}
