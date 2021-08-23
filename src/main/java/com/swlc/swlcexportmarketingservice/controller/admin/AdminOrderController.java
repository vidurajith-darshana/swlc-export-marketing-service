package com.swlc.swlcexportmarketingservice.controller.admin;

import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.service.OrderService;
import com.swlc.swlcexportmarketingservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kavindu
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/order")
public class AdminOrderController {

    private final OrderService orderService;
    private final ProductService productService;

    @Autowired
    public AdminOrderController(OrderService orderService, ProductService productService) {
        this.orderService = orderService;
        this.productService = productService;
    }

    @GetMapping("/top")
    public ResponseEntity<CommonResponseDTO> getTopOrders(@RequestParam("yr") int yr, @RequestParam("mnth") int mnth, HttpServletRequest httpServletRequest) {
        log.info("End point: " + httpServletRequest.getPathInfo());
        return orderService.getAllTopOrders(yr, mnth);
    }

    @GetMapping("/products/top")
    public ResponseEntity<CommonResponseDTO> getTopProducts(@RequestParam("yr") int yr, @RequestParam("mnth") int mnth, HttpServletRequest httpServletRequest) {
        log.info("End point: " + httpServletRequest.getPathInfo());
            return productService.getAllTopProducts(yr, mnth);
    }
}
