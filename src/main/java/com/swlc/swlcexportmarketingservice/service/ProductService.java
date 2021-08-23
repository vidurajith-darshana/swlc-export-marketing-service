package com.swlc.swlcexportmarketingservice.service;

import com.swlc.swlcexportmarketingservice.dto.ProductDTO;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {

    Page<ProductDTO> getAllProducts(Pageable pageable);

    Page<ProductDTO> getAllProductsByAdmin(Pageable pageable);

    ProductDTO saveProduct(ProductDTO productDTO);

    ProductDTO updateProduct(ProductDTO productDTO);

    void updateProductStatus(int productId, ProductStatus productStatus);

    void updateProductCurrentQty(int productId, int currentQty);

    Page<ProductDTO> getAllProductsByCategoryId(int id, Pageable pageable);

    ResponseEntity<CommonResponseDTO> getAllTopProducts(int yr, int mth);
}
