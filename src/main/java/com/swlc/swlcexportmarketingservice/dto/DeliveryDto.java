package com.swlc.swlcexportmarketingservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeliveryDto {

    private int fkUser;
    private String deliveryAddress;
    private String buyerAddress;
    private String mobile;
    private String telephone;
    private String whatsapp;
}
