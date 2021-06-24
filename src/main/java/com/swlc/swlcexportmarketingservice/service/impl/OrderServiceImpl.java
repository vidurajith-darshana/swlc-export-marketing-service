package com.swlc.swlcexportmarketingservice.service.impl;

import com.swlc.swlcexportmarketingservice.dto.OrderDetailDto;
import com.swlc.swlcexportmarketingservice.dto.OrderDto;
import com.swlc.swlcexportmarketingservice.dto.common.CommonOrderDTO;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.entity.Order;
import com.swlc.swlcexportmarketingservice.entity.OrderDetail;
import com.swlc.swlcexportmarketingservice.mapper.OrderDetailMapper;
import com.swlc.swlcexportmarketingservice.mapper.OrderMapper;
import com.swlc.swlcexportmarketingservice.repository.OrderDetailRepository;
import com.swlc.swlcexportmarketingservice.repository.OrderRepository;
import com.swlc.swlcexportmarketingservice.service.OrderService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.swlc.swlcexportmarketingservice.constant.ApplicationConstant.APPLICATION_ERROR_OCCURRED_MESSAGE;
import static com.swlc.swlcexportmarketingservice.constant.ApplicationConstant.REQUEST_SUCCESS_MESSAGE;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    private static final Logger LOGGER = LogManager.getLogger(OrderServiceImpl.class);

    @Override
    public ResponseEntity<CommonResponseDTO> saveOrder(CommonOrderDTO commonOrderDTO) {

        try {
            OrderDto orderDto = new OrderDto(commonOrderDTO.getId(),
                    commonOrderDTO.getFkUserId(),
                    commonOrderDTO.getTotal(), commonOrderDTO.getMessage(), commonOrderDTO.getStatus());

            Order order = orderRepository.save(orderMapper.convertOrderDtoToOrder(orderDto));
            if (order != null) {
                int orderId = order.getId().intValue();
                for (OrderDetailDto orderDetailDto : commonOrderDTO.getOrderDetails()) {
                    orderDetailDto.setOrderId(orderId);
                    orderDetailRepository.save(orderDetailMapper.convertOrderDetailDtoToOrderDetail(orderDetailDto));
                }
                return new ResponseEntity<>(new CommonResponseDTO(true, REQUEST_SUCCESS_MESSAGE, "Order created successfully!"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, "Order Creation fail!"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<CommonResponseDTO> getAllOrders() {
        try {
            List<Order> orderList = orderRepository.findAll();

            List<OrderDetailDto> orderDetailDtoList = new ArrayList<>();
            List<CommonOrderDTO> commonOrderDTOList = new ArrayList<>();

            if (orderList.size() > 0) {
                for (Order order : orderList) {
                    OrderDto orderDto = orderMapper.convertOrderToOrderDto(order);
                    for (OrderDetail orderDetail : order.getFkOrder()) {
                        orderDetailDtoList.add(orderDetailMapper.convertOrderDetailToOrderDetailDto(orderDetail));
                    }
                    CommonOrderDTO commonOrderDTO = new CommonOrderDTO(orderDto.getId(), orderDto.getFkUserId(), orderDto.getTotal(), orderDto.getMessage(), orderDto.getStatus(), orderDetailDtoList);
                    commonOrderDTOList.add(commonOrderDTO);
                }
                return new ResponseEntity<>(new CommonResponseDTO(true, REQUEST_SUCCESS_MESSAGE, commonOrderDTOList), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, "Failed to get all orders!"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
