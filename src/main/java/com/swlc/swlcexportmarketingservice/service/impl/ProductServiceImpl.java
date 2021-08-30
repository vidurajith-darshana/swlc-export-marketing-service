package com.swlc.swlcexportmarketingservice.service.impl;

import com.swlc.swlcexportmarketingservice.dto.CategoryDTO;
import com.swlc.swlcexportmarketingservice.dto.ProductDTO;
import com.swlc.swlcexportmarketingservice.dto.ProductRequestDto;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.dto.response.ProductUserResponseDTO;
import com.swlc.swlcexportmarketingservice.dto.response.Top10ProductsResponseDTO;
import com.swlc.swlcexportmarketingservice.dto.row_data.Top10ProductsRowDataDTO;
import com.swlc.swlcexportmarketingservice.entity.*;
import com.swlc.swlcexportmarketingservice.enums.CategoryStatus;
import com.swlc.swlcexportmarketingservice.enums.ProductReviewStatus;
import com.swlc.swlcexportmarketingservice.enums.ProductStatus;
import com.swlc.swlcexportmarketingservice.exception.SwlcExportMarketException;
import com.swlc.swlcexportmarketingservice.repository.CategoryRepository;
import com.swlc.swlcexportmarketingservice.repository.ProductCategoryRepository;
import com.swlc.swlcexportmarketingservice.repository.ProductRepository;
import com.swlc.swlcexportmarketingservice.repository.ProductReviewRepository;
import com.swlc.swlcexportmarketingservice.service.ProductService;
import com.swlc.swlcexportmarketingservice.util.FileHandler;
import com.swlc.swlcexportmarketingservice.util.HtmlToString;
import com.swlc.swlcexportmarketingservice.util.MailSender;
import com.swlc.swlcexportmarketingservice.util.TokenValidator;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.swlc.swlcexportmarketingservice.constant.ApplicationConstant.*;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final FileHandler fileHandler;
    private final ProductCategoryRepository productCategoryRepository;
    private final TokenValidator tokenValidator;
    private final ProductReviewRepository productReviewRepository;

    @Autowired
    private MailSender mailSender;

    @Value("${server.upload.url}")
    private String archivePath;

    @Value("${admin.mail}")
    private String adminMail;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ModelMapper modelMapper, FileHandler fileHandler, ProductCategoryRepository productCategoryRepository, TokenValidator tokenValidator, ProductReviewRepository productReviewRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.fileHandler = fileHandler;
        this.productCategoryRepository = productCategoryRepository;
        this.tokenValidator = tokenValidator;
        this.productReviewRepository = productReviewRepository;
    }


    @Override
    public Page<ProductUserResponseDTO> getAllProducts(Pageable pageable) {
//        return productRepository.getAllProducts(pageable).map(this::getProductDTO);
        return productRepository.getAllActiveProducts(CategoryStatus.ACTIVE, ProductStatus.ACTIVE, pageable).map(this::getProductDTOForRegisterdUser);
    }

    @Override
    public List<ProductDTO> getAllActiveProducts() {
        try {
            List<Product> allActiveProducts = productRepository.getAllActiveProducts(CategoryStatus.ACTIVE, ProductStatus.ACTIVE);
            List<ProductDTO> productDTOS = new ArrayList<>();
            for (Product p :allActiveProducts) {
                productDTOS.add(this.getProductDTO(p));
            }
            return productDTOS;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Page<ProductDTO> getAllProductsByAdmin(String search, Pageable pageable) {
        return productRepository.getAllProductsWithSearch(search, pageable).map(this::getProductDTO);
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

    ProductUserResponseDTO getProductDTOForRegisterdUser(Product product) {
        ProductUserResponseDTO productDTO = modelMapper.map(product, ProductUserResponseDTO.class);

        User user = tokenValidator.retrieveUserInformationFromAuthentication();

        boolean isLoggedUser = false;
        boolean isUserLiked = false;
        int productReviewCount = productReviewRepository.calProductLikeCount(product);
        if(user!=null) {
            isLoggedUser = true;
            Optional<ProductReviews> productReviewsByUserAndProduct = productReviewRepository.getProductReviewsByUserAndProduct(user, product);
            isUserLiked = productReviewsByUserAndProduct.isPresent();
        }
        productDTO.setLoggedUser(isLoggedUser);
        productDTO.setUserLiked(isUserLiked);
        productDTO.setLikeCount(productReviewCount);

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

            File productRequestHtml = new File(archivePath+"html-templates/product-request.html");
            String html = new HtmlToString().convertHtmlToString(productRequestHtml.getPath());
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

    @Override
    public ResponseEntity<CommonResponseDTO> getAllTopProducts(int yr, int mth) {
        try {
            List<Top10ProductsResponseDTO> top10ProductsDetails = new ArrayList<>();
            List<Top10ProductsRowDataDTO> top10ProductsByYearAndMonth = productRepository.getTop10ProductsByYearAndMonth(yr, mth);

            System.out.println(top10ProductsByYearAndMonth.size());

            for (Top10ProductsRowDataDTO p : top10ProductsByYearAndMonth) {
                System.out.println("X: " + p.getQty());
                Product product = productRepository.findProductById(p.getPid());
                ProductDTO productDTO = this.getProductDTO(product);
                top10ProductsDetails.add(new Top10ProductsResponseDTO(productDTO, p.getQty()));
            }
            return new ResponseEntity<>(new CommonResponseDTO(true, REQUEST_SUCCESS_MESSAGE, top10ProductsDetails), HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity<CommonResponseDTO> likeProduct(int productId,  ProductReviewStatus status) {
        log.info("Execute method likeProduct");
        try {
            User user = tokenValidator.retrieveUserInformationFromAuthentication();
            if(user==null) throw new SwlcExportMarketException(404, "Unable to proceed the action. Invalid user");
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if(!optionalProduct.isPresent()) throw new SwlcExportMarketException(404, "Product not found");
            Optional<ProductReviews> productReviewsByUserAndProduct = productReviewRepository.getProductReviewsByUserAndProduct(user, optionalProduct.get());
            boolean isLike = false;
            if(productReviewsByUserAndProduct.isPresent()){
                ProductReviews productReviews = productReviewsByUserAndProduct.get();
                if(productReviews.getStatus() != null) {
                    productReviews.setStatus(status);
                    productReviewRepository.save(productReviews);
                } else {
                    productReviewRepository.delete(productReviews);
                }
            } else {
                isLike = true;
                productReviewRepository.save(new ProductReviews(user, optionalProduct.get(), new Date(), status));
            }
            return new ResponseEntity<>(new CommonResponseDTO(true, isLike?"Liked successfully!":"Disliked successfully!", null), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Execute method likeProduct: " + e.getMessage());
            throw e;
        }
    }
}
