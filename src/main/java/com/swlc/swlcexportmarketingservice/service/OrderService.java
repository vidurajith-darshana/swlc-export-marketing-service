package com.swlc.swlcexportmarketingservice.service;

import com.swlc.swlcexportmarketingservice.dto.common.CommonOrderDTO;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    ResponseEntity<CommonResponseDTO> saveOrder(CommonOrderDTO commonOrderDTO);

    ResponseEntity<CommonResponseDTO> getAllOrders();

    ResponseEntity<CommonResponseDTO> trackOrder(String orderRef);

    ResponseEntity<CommonResponseDTO> updateOrderStatus(String orderRef, String status);

    ResponseEntity<CommonResponseDTO> updateOrder(CommonOrderDTO commonOrderDTO);

    ResponseEntity<CommonResponseDTO> getAllTopOrders(int yr, int mth);

    ResponseEntity<CommonResponseDTO> getAllCustomerOrdersById(int customerId);

    ResponseEntity<CommonResponseDTO> getAllOrdersByAdmin(String ref);
}
