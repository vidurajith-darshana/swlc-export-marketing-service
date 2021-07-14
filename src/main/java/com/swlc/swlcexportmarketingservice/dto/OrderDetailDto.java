package com.swlc.swlcexportmarketingservice.dto;

import lombok.Data;

@Data
public class OrderDetailDto {
    private int id;
    private int orderId;
    private double qty;
    private double price;
    private double subTotal;
    private int fkProductId;

    public OrderDetailDto(int id, double qty, double price, double subTotal, int fkProductId) {
        this.id = id;
        this.qty = qty;
        this.price = price;
        this.subTotal = subTotal;
        this.fkProductId = fkProductId;
    }
}
