package com.swlc.swlcexportmarketingservice.dto;

import lombok.Data;

@Data
public class OrderDto {
    private int id;
    private int fkUserId;
    private double total;
    private String message;
    private String status;

    public OrderDto(int id, int fkUserId, double total, String message, String status) {
        this.id = id;
        this.fkUserId = fkUserId;
        this.total = total;
        this.message = message;
        this.status = status;
    }
}
