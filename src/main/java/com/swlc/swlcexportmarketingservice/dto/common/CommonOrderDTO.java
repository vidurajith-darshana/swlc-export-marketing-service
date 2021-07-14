package com.swlc.swlcexportmarketingservice.dto.common;

import com.swlc.swlcexportmarketingservice.dto.OrderDetailDto;
import com.swlc.swlcexportmarketingservice.entity.OrderDetail;
import lombok.Data;

import java.util.List;

@Data
public class CommonOrderDTO {
    private int id;
    private int fkUserId;
    private double total;
    private String message;
    private String status;
    private List<OrderDetailDto> orderDetails;

    public CommonOrderDTO(int id, int fkUserId, double total, String message, String status, List<OrderDetailDto> orderDetails) {
        this.id = id;
        this.fkUserId = fkUserId;
        this.total = total;
        this.message = message;
        this.status = status;
        this.orderDetails = orderDetails;
    }
}
