package com.swlc.swlcexportmarketingservice.controller.admin;

import com.swlc.swlcexportmarketingservice.dto.CategoryDTO;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin")
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/category/create")
    public ResponseEntity<CommonResponseDTO> createCategory(@RequestBody CategoryDTO categoryDTO, HttpServletRequest httpServletRequest) {

        log.info("End point: " + httpServletRequest.getPathInfo());

        categoryDTO = categoryService.saveCategory(categoryDTO);

        return ResponseEntity.ok(new CommonResponseDTO(true, categoryDTO));
    }

    @GetMapping("/category/all")
    public ResponseEntity<CommonResponseDTO> getAllCategories(Pageable pageable, HttpServletRequest httpServletRequest) {

        log.info("End point: " + httpServletRequest.getPathInfo());

        log.info("Page : {}",pageable);

        Page<CategoryDTO> allCategory = categoryService.getAllCategory(pageable);

        return ResponseEntity.ok(new CommonResponseDTO(true, allCategory));

    }

    @PutMapping("/category")
    public ResponseEntity<CommonResponseDTO> updateCategory(@RequestBody CategoryDTO categoryDTO, HttpServletRequest httpServletRequest) {

        log.info("End point: " + httpServletRequest.getPathInfo());

        categoryDTO = categoryService.updateCategory(categoryDTO);

        return ResponseEntity.ok(new CommonResponseDTO(true, categoryDTO));
    }
}
