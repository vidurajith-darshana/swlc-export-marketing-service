package com.swlc.swlcexportmarketingservice.service.impl;

import com.swlc.swlcexportmarketingservice.dto.CategoryDTO;
import com.swlc.swlcexportmarketingservice.dto.ProductDTO;
import com.swlc.swlcexportmarketingservice.dto.ProductRequestDto;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.entity.Category;
import com.swlc.swlcexportmarketingservice.entity.Product;
import com.swlc.swlcexportmarketingservice.entity.ProductCategory;
import com.swlc.swlcexportmarketingservice.enums.CategoryStatus;
import com.swlc.swlcexportmarketingservice.enums.ProductStatus;
import com.swlc.swlcexportmarketingservice.exception.SwlcExportMarketException;
import com.swlc.swlcexportmarketingservice.repository.CategoryRepository;
import com.swlc.swlcexportmarketingservice.repository.ProductCategoryRepository;
import com.swlc.swlcexportmarketingservice.repository.ProductRepository;
import com.swlc.swlcexportmarketingservice.service.ProductService;
import com.swlc.swlcexportmarketingservice.util.FileHandler;
import com.swlc.swlcexportmarketingservice.util.HtmlToString;
import com.swlc.swlcexportmarketingservice.util.MailSender;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.swlc.swlcexportmarketingservice.constant.ApplicationConstant.*;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final FileHandler fileHandler;
    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    private MailSender mailSender;

    @Value("classpath:html-templates/product-request.html")
    Resource productRequestHtml;

    @Value("${admin.mail}")
    private String adminMail;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ModelMapper modelMapper, FileHandler fileHandler, ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.fileHandler = fileHandler;
        this.productCategoryRepository = productCategoryRepository;
    }


    @Override
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
//        return productRepository.getAllProducts(pageable).map(this::getProductDTO);
        return productRepository.getAllActiveProducts(CategoryStatus.ACTIVE, ProductStatus.ACTIVE, pageable).map(this::getProductDTO);
    }

    @Override
    public Page<ProductDTO> getAllProductsByAdmin(Pageable pageable) {
        return productRepository.getAllProducts(pageable).map(this::getProductDTO);
    }

    @Override
    public ProductDTO saveProduct(ProductDTO productDTO) {

        if (productDTO == null) {
            throw new SwlcExportMarketException(404, NOT_FOUND_PRODUCT);
        }

        List<CategoryDTO> categoryDTOs = productDTO.getCategories();
        List<CategoryDTO> categoryDTOsWithFullData = new ArrayList<>();

        if (categoryDTOs.size() == 0) {
            throw new SwlcExportMarketException(404, NOT_FOUND_CATEGORY);
        }

        List<Category> categories = new ArrayList<>();

        for (CategoryDTO c : categoryDTOs) {
            Optional<Category> optionalCategory = categoryRepository.findById(c.getId());

            if (!optionalCategory.isPresent()) {
                throw new SwlcExportMarketException(404, NOT_FOUND_CATEGORY);
            }

            Category category = optionalCategory.get();

            categories.add(category);
            
            categoryDTOsWithFullData.add(modelMapper.map(category,CategoryDTO.class));
            
        }

        if (productDTO.getThumbnail() == null) {
            throw new SwlcExportMarketException(404, NOT_FOUND_THUMBNAIL);
        }

        String imageFile = fileHandler.saveImageFile(productDTO.getThumbnail());

        productDTO.setThumbnail(imageFile);

        Product product = modelMapper.map(productDTO, Product.class);
        
        product.setStatus(ProductStatus.ACTIVE);

        product = productRepository.save(product);

        for (Category category : categories) {

            ProductCategory productCategory = new ProductCategory();
            productCategory.setFkProduct(product);
            productCategory.setFkCategory(category);

            productCategoryRepository.save(productCategory);
        }

        ProductDTO dto = modelMapper.map(product, ProductDTO.class);

        dto.setCategories(categoryDTOsWithFullData);

        return dto;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO) {

        if (productDTO == null) {
            throw new SwlcExportMarketException(404, NOT_FOUND_PRODUCT);
        }

        List<CategoryDTO> categoryDTOs = productDTO.getCategories();

        if (categoryDTOs.size() == 0) {
            throw new SwlcExportMarketException(404, NOT_FOUND_CATEGORY);
        }

        List<Category> categories = new ArrayList<>();

        for (CategoryDTO c : categoryDTOs) {
            Optional<Category> optionalCategory = categoryRepository.findById(c.getId());

            if (!optionalCategory.isPresent()) {
                throw new SwlcExportMarketException(404, NOT_FOUND_CATEGORY);
            }

            Category category = optionalCategory.get();

            categories.add(category);
        }

        if (productDTO.getThumbnail() != null) {
            String imageFile = fileHandler.saveImageFile(productDTO.getThumbnail());

            productDTO.setThumbnail(imageFile);
        }

        Product product = modelMapper.map(productDTO, Product.class);

        product = productRepository.save(product);

        List<ProductCategory> productCategories = productCategoryRepository.findByFkProduct(product);

        //delete previous product categories
        for (ProductCategory pc : productCategories) {
            productCategoryRepository.delete(pc);
        }

        for (Category category : categories) {

            ProductCategory productCategory = new ProductCategory();
            productCategory.setFkProduct(product);
            productCategory.setFkCategory(category);

            productCategoryRepository.save(productCategory);
        }

        productDTO = modelMapper.map(product, ProductDTO.class);

        productDTO.setCategories(categoryDTOs);

        return productDTO;
    }

    @Override
    public void updateProductStatus(int productId, ProductStatus productStatus) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        
        if (!optionalProduct.isPresent()){
            throw new SwlcExportMarketException(404, NOT_FOUND_PRODUCT);
        }

        Product product = optionalProduct.get();
        product.setStatus(productStatus);
        productRepository.save(product);

    }

    @Override
    public void updateProductCurrentQty(int productId, int currentQty) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (!optionalProduct.isPresent()){
            throw new SwlcExportMarketException(404, NOT_FOUND_PRODUCT);
        }

        Product product = optionalProduct.get();
        product.setCurrentQty(currentQty);
        productRepository.save(product);
    }



    ProductDTO getProductDTO(Product product) {
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

        List<ProductCategory> productCategories = productCategoryRepository.findByFkProduct(product);

        List<CategoryDTO> categoryDTOS = new ArrayList<>();

        for (ProductCategory pc : productCategories) {
            categoryDTOS.add(modelMapper.map(pc.getFkCategory(), CategoryDTO.class));
        }

        productDTO.setCategories(categoryDTOS);

        return productDTO;
    }

    @Override
    public Page<ProductDTO> getAllProductsByCategoryId(int id, Pageable pageable) {
        try {
            Optional<Category> byId = categoryRepository.findById(id);
            if(!byId.isPresent()) throw new SwlcExportMarketException(404, "Category not found");
            Page<ProductDTO> map = productCategoryRepository.getProductByFkCategory(byId.get(), CategoryStatus.ACTIVE, pageable).map(this::getProductDTO);
            return map;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity<CommonResponseDTO> requestProductDetails(ProductRequestDto productRequestDto) {
        try {

            String html = new HtmlToString().convertHtmlToString(productRequestHtml.getFile().getPath());
            html = html.replace("xProductCode",productRequestDto.getProductCode());
            html = html.replace("xProductName",productRequestDto.getProductName());
            html = html.replace("xCustomerName",productRequestDto.getCustomerName());
            html = html.replace("xEmail",productRequestDto.getEmail());
            html = html.replace("xDescription",productRequestDto.getDescription());

            mailSender.sendEmail(adminMail,null, PRODUCT_REQUEST_EMAIL_SUBJECT, html, true, null);

            return new ResponseEntity<>(new CommonResponseDTO(true, REQUEST_SUCCESS_MESSAGE, "Request sent successfully!"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
