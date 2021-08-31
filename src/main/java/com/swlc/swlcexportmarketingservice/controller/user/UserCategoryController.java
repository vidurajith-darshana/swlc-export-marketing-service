package com.swlc.swlcexportmarketingservice.controller.user;

import com.swlc.swlcexportmarketingservice.dto.CategoryDTO;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/v1/user/category")
public class UserCategoryController {

    private final CategoryService categoryService;

    public UserCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<CommonResponseDTO> getAllCategories(Pageable pageable, HttpServletRequest httpServletRequest) {

        log.info("End point: " + httpServletRequest.getPathInfo());

        log.info("Page : {}",pageable);

        Page<CategoryDTO> allCategory = categoryService.getAllCategory(pageable);

        return ResponseEntity.ok(new CommonResponseDTO(true, allCategory));

    }

    @PostMapping("/status/update/{categoryId}/{status}")
    public ResponseEntity<CommonResponseDTO> updateCategoryStatus(@PathVariable("categoryId") int categoryId,@PathVariable("status") int status) {

        return categoryService.updateCategoryStatus(categoryId,status);

    }
}
