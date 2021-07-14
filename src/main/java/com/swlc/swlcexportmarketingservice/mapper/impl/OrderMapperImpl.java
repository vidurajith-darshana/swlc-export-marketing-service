package com.swlc.swlcexportmarketingservice.mapper.impl;

import com.swlc.swlcexportmarketingservice.dto.OrderDto;
import com.swlc.swlcexportmarketingservice.entity.Order;
import com.swlc.swlcexportmarketingservice.entity.User;
import com.swlc.swlcexportmarketingservice.mapper.OrderMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderMapperImpl implements OrderMapper {
    @Override
    public OrderDto convertOrderToOrderDto(Order order) {
        if (order == null){
            return null;
        }

        return new OrderDto(
                order.getId().intValue(),
                order.getFkUser().getId().intValue(),
                order.getTotal().doubleValue(),
                order.getMessage(),
                order.getStatus()
        );
    }

    @Override
    public Order convertOrderDtoToOrder(OrderDto orderDto) {
        if (orderDto == null){
            return null;
        }
        return new Order(
                orderDto.getId(),
                new User(orderDto.getFkUserId()),
                orderDto.getTotal(),
                orderDto.getMessage(),
                orderDto.getStatus()
        );
    }
}
