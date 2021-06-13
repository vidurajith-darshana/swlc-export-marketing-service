package com.swlc.swlcexportmarketingservice.service.impl;

import com.swlc.swlcexportmarketingservice.dto.CategoryDTO;
import com.swlc.swlcexportmarketingservice.entity.Category;
import com.swlc.swlcexportmarketingservice.exception.SwlcExportMarketException;
import com.swlc.swlcexportmarketingservice.repository.CategoryRepository;
import com.swlc.swlcexportmarketingservice.service.CategoryService;
import com.swlc.swlcexportmarketingservice.util.FileHandler;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import static com.swlc.swlcexportmarketingservice.constant.ApplicationConstant.NOT_FOUND_CATEGORY;

@Service
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final FileHandler fileHandler;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper, FileHandler fileHandler) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.fileHandler = fileHandler;
    }


    @Override
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {

        if (categoryDTO == null) {
            throw new SwlcExportMarketException(500, NOT_FOUND_CATEGORY);
        }

        String thumbnail = fileHandler.saveImageFile(categoryDTO.getThumbnail());

        Category category = modelMapper.map(categoryDTO, Category.class);

        category.setThumbnail(thumbnail);

        category = categoryRepository.save(category);

        return modelMapper.map(category, CategoryDTO.class);

    }
}
