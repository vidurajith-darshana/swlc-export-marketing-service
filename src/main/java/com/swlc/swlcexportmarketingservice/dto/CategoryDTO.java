package com.swlc.swlcexportmarketingservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDTO {
    private int id;
    private String name;
    private String thumbnail;
    private Date createDate;
}
