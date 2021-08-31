package com.swlc.swlcexportmarketingservice.service.impl;

import com.swlc.swlcexportmarketingservice.dto.CategoryDTO;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.entity.Category;
import com.swlc.swlcexportmarketingservice.enums.CategoryStatus;
import com.swlc.swlcexportmarketingservice.exception.SwlcExportMarketException;
import com.swlc.swlcexportmarketingservice.repository.CategoryRepository;
import com.swlc.swlcexportmarketingservice.service.CategoryService;
import com.swlc.swlcexportmarketingservice.util.FileHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.swlc.swlcexportmarketingservice.constant.ApplicationConstant.*;

@Service
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final FileHandler fileHandler;

    private static final Logger LOGGER = LogManager.getLogger(CategoryServiceImpl.class);

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper, FileHandler fileHandler) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.fileHandler = fileHandler;
    }


    @Override
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {

        if (categoryDTO == null) {
            throw new SwlcExportMarketException(404, NOT_FOUND_CATEGORY);
        }

        if (categoryDTO.getThumbnail() == null){
            throw new SwlcExportMarketException(404,NOT_FOUND_THUMBNAIL);
        }

        if (categoryRepository.findCategoryByName(categoryDTO.getName()) != null) {
            throw new SwlcExportMarketException(500,CATEGORY_EXISTS);
        }

        String thumbnail = fileHandler.saveImageFile(categoryDTO.getThumbnail());

        Category category = modelMapper.map(categoryDTO, Category.class);

        category.setThumbnail(thumbnail);

        category.setCategoryStatus(CategoryStatus.ACTIVE);

        category = categoryRepository.save(category);

        return modelMapper.map(category, CategoryDTO.class);

    }

    @Override
    public Page<CategoryDTO> getAllCategory(Pageable pageable) {
//        return categoryRepository.getAllCategories(pageable).map(this::getCategoryDTO);
        return categoryRepository.getAllCategoriesByCustomer(pageable).map(this::getCategoryDTO);
    }

    @Override
    public Page<CategoryDTO> getAllCategoryByAdmin(String search, Pageable pageable) {
        return categoryRepository.getAllCategoriesWithSearch(search, pageable).map(this::getCategoryDTO);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {

        if (categoryDTO == null) {
            throw new SwlcExportMarketException(404, NOT_FOUND_CATEGORY);
        }

        Optional<Category> optionalCategory = categoryRepository.findById(categoryDTO.getId());

        if (!optionalCategory.isPresent()) {
            throw new SwlcExportMarketException(404, NOT_FOUND_CATEGORY);
        }

        Category category = optionalCategory.get();

        category.setName(categoryDTO.getName());

        if (categoryDTO.getThumbnail() != null) {
            String thumbnail = fileHandler.saveImageFile(categoryDTO.getThumbnail());

            category.setThumbnail(thumbnail);
        }

        category.setCategoryStatus(categoryDTO.getCategoryStatus());

        category = categoryRepository.save(category);

        return modelMapper.map(category,CategoryDTO.class);
    }

    @Override
    public ResponseEntity<CommonResponseDTO> updateCategoryStatus(int categoryId, int status) {
        try {
            Category category = categoryRepository.findCategoryById(categoryId);
            if (category != null) {
                if (status == 0) {
                    category.setCategoryStatus(CategoryStatus.INACTIVE);
                } else {
                    category.setCategoryStatus(CategoryStatus.ACTIVE);
                }
                categoryRepository.save(category);
                return new ResponseEntity<>(new CommonResponseDTO(true, REQUEST_SUCCESS_MESSAGE, "Success!"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, "Unable to find category!"), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private CategoryDTO getCategoryDTO(Category category) {
        return modelMapper.map(category, CategoryDTO.class);
    }
}
