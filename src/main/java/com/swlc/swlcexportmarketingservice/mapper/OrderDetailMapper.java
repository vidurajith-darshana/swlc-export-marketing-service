package com.swlc.swlcexportmarketingservice.mapper;

import com.swlc.swlcexportmarketingservice.dto.OrderDetailDto;
import com.swlc.swlcexportmarketingservice.entity.OrderDetail;

public interface OrderDetailMapper {
    OrderDetailDto convertOrderDetailToOrderDetailDto(OrderDetail orderDetail);
    OrderDetail convertOrderDetailDtoToOrderDetail(OrderDetailDto orderDetailDto);
}
