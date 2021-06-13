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
        return null;
    }

    @Override
    public Order convertOrderDtoToOrder(OrderDto orderDto) {
        if (orderDto == null){
            return null;
        }
        return null;
    }
}
