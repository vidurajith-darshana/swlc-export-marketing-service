package com.swlc.swlcexportmarketingservice.mapper.impl;

import com.swlc.swlcexportmarketingservice.dto.OrderDetailDto;
import com.swlc.swlcexportmarketingservice.entity.Order;
import com.swlc.swlcexportmarketingservice.entity.OrderDetail;
import com.swlc.swlcexportmarketingservice.entity.Product;
import com.swlc.swlcexportmarketingservice.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailMapperImpl implements OrderDetailMapper {
    @Override
    public OrderDetailDto convertOrderDetailToOrderDetailDto(OrderDetail orderDetail) {
        return null;
    }

    @Override
    public OrderDetail convertOrderDetailDtoToOrderDetail(OrderDetailDto orderDetailDto) {
        if (orderDetailDto == null){
            return null;
        }
        return new OrderDetail(
                orderDetailDto.getId(),
                orderDetailDto.getQty(),
                orderDetailDto.getPrice(),
                orderDetailDto.getSubTotal(),
                new Product(orderDetailDto.getFkProductId()),
                new Order(orderDetailDto.getOrderId())
        );
    }
}
