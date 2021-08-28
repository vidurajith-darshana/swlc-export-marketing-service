package com.swlc.swlcexportmarketingservice.service;

import com.swlc.swlcexportmarketingservice.dto.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {

    CategoryDTO saveCategory(CategoryDTO categoryDTO);

    Page<CategoryDTO> getAllCategory(Pageable pageable);

    Page<CategoryDTO> getAllCategoryByAdmin(String search, Pageable pageable);

    CategoryDTO updateCategory(CategoryDTO categoryDTO);

}
