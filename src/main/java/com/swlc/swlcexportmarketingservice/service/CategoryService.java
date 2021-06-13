package com.swlc.swlcexportmarketingservice.service;

import com.swlc.swlcexportmarketingservice.dto.CategoryDTO;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {

    CategoryDTO saveCategory(CategoryDTO categoryDTO);

}
