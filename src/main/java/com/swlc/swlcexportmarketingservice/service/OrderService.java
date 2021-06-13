package com.swlc.swlcexportmarketingservice.service;

import com.swlc.swlcexportmarketingservice.dto.common.CommonOrderDTO;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    ResponseEntity<CommonResponseDTO> saveOrder(CommonOrderDTO commonOrderDTO);
}
