package com.swlc.swlcexportmarketingservice.dto.response;

import com.swlc.swlcexportmarketingservice.dto.CategoryDTO;
import com.swlc.swlcexportmarketingservice.enums.ProductStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author hp
 */

@NoArgsConstructor
@Getter
@Setter
public class ProductUserResponseDTO {
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
    private int likeCount;
    private boolean LoggedUser;
    private boolean isUserLiked;
}
