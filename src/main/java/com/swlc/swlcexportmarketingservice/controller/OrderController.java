package com.swlc.swlcexportmarketingservice.controller;

import com.swlc.swlcexportmarketingservice.constant.ApplicationConstant;
import com.swlc.swlcexportmarketingservice.dto.common.CommonOrderDTO;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.swlc.swlcexportmarketingservice.constant.ApplicationConstant.REQUEST_SUCCESS_MESSAGE;

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

    @PostMapping(value = "/update")
    public ResponseEntity<CommonResponseDTO> updateOrder(@RequestBody CommonOrderDTO commonOrderDTO){
        return orderService.updateOrder(commonOrderDTO);
    }

    @GetMapping(value = "/getOrderStatuses")
    public ResponseEntity<CommonResponseDTO> getOrderStatuses(){
        String[] array = new String[]{
                ApplicationConstant.ORDER_STATUS.REVIEWING.toString(),
                ApplicationConstant.ORDER_STATUS.IN_PROGRESS.toString(),
                ApplicationConstant.ORDER_STATUS.DISPATCHED.toString(),
                ApplicationConstant.ORDER_STATUS.DELIVERED.toString()};
        return new ResponseEntity<>(new CommonResponseDTO(true, REQUEST_SUCCESS_MESSAGE, array), HttpStatus.OK);
    }

    @GetMapping(value = "/trackOrderByRef/{orderRef}")
    public ResponseEntity<CommonResponseDTO> trackOrder(@PathVariable("orderRef") String orderRef){
        return orderService.trackOrder(orderRef);
    }

    @PostMapping(value = "/update/status/{ref}/{status}")
    public ResponseEntity<CommonResponseDTO> updateOrderStatus(@PathVariable("ref") String ref, @PathVariable("status") String status){
        return orderService.updateOrderStatus(ref,status);
    }
}
