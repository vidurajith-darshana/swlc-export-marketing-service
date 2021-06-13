package com.swlc.swlcexportmarketingservice.dto.common;

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
    private List<OrderDetail> orderDetails;
}
