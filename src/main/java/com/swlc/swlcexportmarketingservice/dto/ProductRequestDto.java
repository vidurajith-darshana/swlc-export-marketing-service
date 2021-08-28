package com.swlc.swlcexportmarketingservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductRequestDto {

    private String productCode;
    private String productName;
    private String customerName;
    private String email;
    private String description;
}
