package com.swlc.swlcexportmarketingservice.dto.response;

import com.swlc.swlcexportmarketingservice.dto.PromotionCommentDTO;
import com.swlc.swlcexportmarketingservice.enums.PromotionStatus;
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
public class PromotionUserResponseDTO {
    private int id;
    private PromotionStatus status;
    private String image;
    private String description;
    private String heading;
    private Date createDate;
    private int likeCount;
    private int dislikeCount;
    private boolean LoggedUser;
    private int isUserLiked;
    private List<PromotionCommentDTO> commentList;
}
