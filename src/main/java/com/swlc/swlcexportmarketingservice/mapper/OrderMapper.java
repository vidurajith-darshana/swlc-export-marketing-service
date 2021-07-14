package com.swlc.swlcexportmarketingservice.mapper;

import com.swlc.swlcexportmarketingservice.dto.OrderDto;
import com.swlc.swlcexportmarketingservice.entity.Order;

public interface OrderMapper {
    OrderDto convertOrderToOrderDto(Order order);
    Order convertOrderDtoToOrder(OrderDto orderDto);
}
