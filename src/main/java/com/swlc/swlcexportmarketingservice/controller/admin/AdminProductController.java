package com.swlc.swlcexportmarketingservice.controller.admin;

import com.swlc.swlcexportmarketingservice.dto.ProductDTO;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.enums.ProductStatus;
import com.swlc.swlcexportmarketingservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/product")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<CommonResponseDTO> getAllProducts(Pageable pageable, HttpServletRequest httpServletRequest) {

        log.info("End point: " + httpServletRequest.getPathInfo());

        log.info("Page : {}",pageable);

        Page<ProductDTO> allProducts = productService.getAllProducts(pageable);

        return ResponseEntity.ok(new CommonResponseDTO(true, allProducts));

    }

    @PostMapping("/create")
    public ResponseEntity<CommonResponseDTO> createCategory(@RequestBody ProductDTO productDTO, HttpServletRequest httpServletRequest) {

        log.info("End point: " + httpServletRequest.getPathInfo());

        ProductDTO saveProduct = productService.saveProduct(productDTO);

        return ResponseEntity.ok(new CommonResponseDTO(true, saveProduct));
    }

    @PutMapping("/update")
    public ResponseEntity<CommonResponseDTO> updateCategory(@RequestBody ProductDTO productDTO, HttpServletRequest httpServletRequest) {

        log.info("End point: " + httpServletRequest.getPathInfo());

        productDTO = productService.updateProduct(productDTO);

        return ResponseEntity.ok(new CommonResponseDTO(true, productDTO));
    }

    @PutMapping("/update/status")
    public ResponseEntity<CommonResponseDTO> updateCategory(@RequestParam(value = "productId") int productId, @RequestParam(value = "productStatus") ProductStatus productStatus, HttpServletRequest httpServletRequest) {

        log.info("End point: " + httpServletRequest.getPathInfo());

        log.info("productId : {} productStatus : {}",productId,productStatus);

        productService.updateProductStatus(productId,productStatus);

        return ResponseEntity.ok(new CommonResponseDTO(true, "Product status has been successfully changed!"));
    }

    @PutMapping("/update/currentqty")
    public ResponseEntity<CommonResponseDTO> updateCurrentQty(@RequestParam(value = "productId") int productId, @RequestParam(value = "currentQty") int currentQty, HttpServletRequest httpServletRequest) {

        log.info("End point: " + httpServletRequest.getPathInfo());

        log.info("productId : {} currentQty : {}",productId,currentQty);

        productService.updateProductCurrentQty(productId,currentQty);

        return ResponseEntity.ok(new CommonResponseDTO(true, "Product current quantity has been successfully changed!"));
    }
}
