package com.swlc.swlcexportmarketingservice.controller.admin;

import com.swlc.swlcexportmarketingservice.dto.CategoryDTO;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
