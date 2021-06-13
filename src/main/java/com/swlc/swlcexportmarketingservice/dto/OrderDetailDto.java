package com.swlc.swlcexportmarketingservice.dto;

import lombok.Data;

@Data
public class OrderDetailDto {
    private int id;
    private double qty;
    private double price;
    private double subTotal;
    private int fkProductId;

    public OrderDetailDto(double qty, double price, double subTotal, int fkProductId) {
        this.qty = qty;
        this.price = price;
        this.subTotal = subTotal;
        this.fkProductId = fkProductId;
    }
}
