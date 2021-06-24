package com.swlc.swlcexportmarketingservice.controller;

import com.swlc.swlcexportmarketingservice.dto.common.CommonOrderDTO;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/save")
    public ResponseEntity<CommonResponseDTO> saveOrder(@RequestBody CommonOrderDTO commonOrderDTO){
        return orderService.saveOrder(commonOrderDTO);
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<CommonResponseDTO> getAllOrders(){
        return orderService.getAllOrders();
    }



}
