package com.swlc.swlcexportmarketingservice.service.impl;

import com.swlc.swlcexportmarketingservice.dto.common.CommonOrderDTO;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.repository.OrderRepository;
import com.swlc.swlcexportmarketingservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public ResponseEntity<CommonResponseDTO> saveOrder(CommonOrderDTO commonOrderDTO) {

        return null;
    }
}
