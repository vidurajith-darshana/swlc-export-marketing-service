package com.swlc.swlcexportmarketingservice.controller.user;

import com.swlc.swlcexportmarketingservice.dto.ProductDTO;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/v1/user/product")
public class UserProductController {

    private final ProductService productService;


    public UserProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<CommonResponseDTO> getAllProducts(Pageable pageable, HttpServletRequest httpServletRequest) {

        log.info("End point: " + httpServletRequest.getPathInfo());

        log.info("Page : {}",pageable);

        Page<ProductDTO> allProducts = productService.getAllProducts(pageable);

        return ResponseEntity.ok(new CommonResponseDTO(true, allProducts));

    }
}
