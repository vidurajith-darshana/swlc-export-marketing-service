package com.swlc.swlcexportmarketingservice.dto;

import com.swlc.swlcexportmarketingservice.entity.User;
import com.swlc.swlcexportmarketingservice.enums.CustomerReviewType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author hp
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerReviewsDto {
    private Integer id;
    private CustomerReviewType customerReviewType;
    private String customerReviewComment;
    private Date customerReviewDate;
    private UserDto user;
}
