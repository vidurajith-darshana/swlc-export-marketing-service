package com.swlc.swlcexportmarketingservice.dto;

import com.swlc.swlcexportmarketingservice.enums.ProductStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ProductDTO {

    private Integer id;
    private String code;
    private String name;
    private String thumbnail;
    private Double price;
    private ProductStatus status;
    private Integer totalQty;
    private Integer currentQty;
    private Date createDate;
    private List<CategoryDTO> categories;
}
