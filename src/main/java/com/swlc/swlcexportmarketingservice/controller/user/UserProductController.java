package com.swlc.swlcexportmarketingservice.controller.user;

import com.swlc.swlcexportmarketingservice.dto.ProductDTO;
import com.swlc.swlcexportmarketingservice.dto.ProductRequestDto;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.dto.response.ProductUserResponseDTO;
import com.swlc.swlcexportmarketingservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

        Page<ProductUserResponseDTO> allProducts = productService.getAllProducts(pageable);

        return ResponseEntity.ok(new CommonResponseDTO(true, allProducts));

    }

    @GetMapping("/active")
    public ResponseEntity<CommonResponseDTO> getAllActiveProducts(HttpServletRequest httpServletRequest) {

        log.info("End point: " + httpServletRequest.getPathInfo());

        List<ProductDTO> allProducts = productService.getAllActiveProducts();

        return ResponseEntity.ok(new CommonResponseDTO(true, allProducts));

    }

    @GetMapping("/all/category")
    public ResponseEntity<CommonResponseDTO> getAllProductsByCategoryId(@RequestParam("category") int categoryId, Pageable pageable, HttpServletRequest httpServletRequest) {

        log.info("End point: " + httpServletRequest.getPathInfo());

        log.info("Page : {}",pageable);

        Page<ProductDTO> allProducts = productService.getAllProductsByCategoryId(categoryId,  pageable);

        return ResponseEntity.ok(new CommonResponseDTO(true, allProducts));

    }

    @PostMapping("/request-details")
    public ResponseEntity<CommonResponseDTO> requestProductDetails(@RequestBody ProductRequestDto productRequestDto) {

        return productService.requestProductDetails(productRequestDto);
    }
}
