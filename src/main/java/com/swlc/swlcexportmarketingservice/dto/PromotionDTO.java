package com.swlc.swlcexportmarketingservice.dto;

import com.swlc.swlcexportmarketingservice.enums.PromotionStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class PromotionDTO {

    private int id;
    private PromotionStatus status;
    private String image;
    private String description;
    private String heading;
    private Date createDate;
}
