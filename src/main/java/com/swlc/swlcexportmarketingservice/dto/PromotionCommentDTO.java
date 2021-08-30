package com.swlc.swlcexportmarketingservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author hp
 */
@NoArgsConstructor
@Getter
@Setter
public class PromotionCommentDTO {
    private String name;
    private Date date;
    private String comment;
}
