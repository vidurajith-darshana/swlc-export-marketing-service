package com.swlc.swlcexportmarketingservice.dto;

import lombok.Data;

@Data
public class OrderDto {
    private int id;
    private int fkUserId;
    private double total;
    private String message;
    private String status;
}
