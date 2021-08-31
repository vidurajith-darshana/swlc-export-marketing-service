package com.swlc.swlcexportmarketingservice.service.impl;

import com.swlc.swlcexportmarketingservice.dto.PromotionCommentDTO;
import com.swlc.swlcexportmarketingservice.dto.PromotionDTO;
import com.swlc.swlcexportmarketingservice.dto.response.PromotionUserResponseDTO;
import com.swlc.swlcexportmarketingservice.entity.Promotion;
import com.swlc.swlcexportmarketingservice.entity.User;
import com.swlc.swlcexportmarketingservice.entity.UserPromotion;
import com.swlc.swlcexportmarketingservice.entity.UserPromotionComment;
import com.swlc.swlcexportmarketingservice.enums.PromotionLikeStatus;
import com.swlc.swlcexportmarketingservice.enums.PromotionStatus;
import com.swlc.swlcexportmarketingservice.exception.SwlcExportMarketException;
import com.swlc.swlcexportmarketingservice.repository.PromotionRepository;
import com.swlc.swlcexportmarketingservice.repository.UserPromotionCommentRepository;
import com.swlc.swlcexportmarketingservice.repository.UserPromotionRepository;
import com.swlc.swlcexportmarketingservice.service.PromotionService;
import com.swlc.swlcexportmarketingservice.util.FileHandler;
import com.swlc.swlcexportmarketingservice.util.TokenValidator;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.swlc.swlcexportmarketingservice.constant.ApplicationConstant.NOT_FOUND_PROMOTION;

@Slf4j
@Service
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final FileHandler fileHandler;
    private final ModelMapper modelMapper;
    private final UserPromotionRepository userPromotionRepository;
    private final TokenValidator tokenValidator;
    private final UserPromotionCommentRepository userPromotionCommentRepository;

    public PromotionServiceImpl(PromotionRepository promotionRepository, FileHandler fileHandler, ModelMapper modelMapper, UserPromotionRepository userPromotionRepository, TokenValidator tokenValidator, UserPromotionCommentRepository userPromotionCommentRepository) {
        this.promotionRepository = promotionRepository;
        this.fileHandler = fileHandler;
        this.modelMapper = modelMapper;
        this.userPromotionRepository = userPromotionRepository;
        this.tokenValidator = tokenValidator;
        this.userPromotionCommentRepository = userPromotionCommentRepository;
    }


    @Override
    public PromotionDTO savePromotion(PromotionDTO promotionDTO) {

        if (promotionDTO == null){
            throw new SwlcExportMarketException(404,NOT_FOUND_PROMOTION);
        }

        String image = promotionDTO.getImage();

        if (image != null){
           promotionDTO.setImage(fileHandler.saveImageFile(image));
        }


        Promotion promotion = modelMapper.map(promotionDTO, Promotion.class);

        promotion.setStatus(PromotionStatus.ACTIVE);

        promotion = promotionRepository.save(promotion);

        return modelMapper.map(promotion,PromotionDTO.class);
    }

    @Override
    public PromotionDTO updatePromotion(PromotionDTO promotionDTO) {

        if (promotionDTO == null){
            throw new SwlcExportMarketException(404,NOT_FOUND_PROMOTION);
        }

        Optional<Promotion> optionalPromotion = promotionRepository.findById(promotionDTO.getId());

        if (!optionalPromotion.isPresent()) throw new SwlcExportMarketException(404,NOT_FOUND_PROMOTION);

        if(promotionDTO.getImage() !=null){
            promotionDTO.setImage(fileHandler.saveImageFile(promotionDTO.getImage()));
        }

        Promotion promotion = modelMapper.map(promotionDTO, Promotion.class);

        promotion = promotionRepository.save(promotion);

        return modelMapper.map(promotion,PromotionDTO.class);
    }

    @Override
    public Page<PromotionUserResponseDTO> getAllPromotions(Pageable pageable) {
        return promotionRepository.getAllPromotions(pageable).map(this::getPromotionUserDTO);
    }

    @Override
    public void deletePromotion(int promotionId) {
        Optional<Promotion> optionalPromotion = promotionRepository.findById(promotionId);

        if (!optionalPromotion.isPresent()) throw new SwlcExportMarketException(404,NOT_FOUND_PROMOTION);

        promotionRepository.deleteById(promotionId);
    }

    @Override
    public void updatePromotionStatus(int promotionId, PromotionStatus status) {
        Optional<Promotion> optionalPromotion = promotionRepository.findById(promotionId);

        if (!optionalPromotion.isPresent()) throw new SwlcExportMarketException(404,NOT_FOUND_PROMOTION);

        Promotion promotion = optionalPromotion.get();

        promotion.setStatus(status);

        promotionRepository.save(promotion);
    }

    @Override
    public void likePromotion(int promotionId, PromotionLikeStatus promotionStatus) {
        log.info("Execute method likePromotion");
        try {

            User user = tokenValidator.retrieveUserInformationFromAuthentication();
            if(user == null) throw new SwlcExportMarketException(404, "User not found");

            Optional<Promotion> promotionOptional = promotionRepository.findById(promotionId);
            if(!promotionOptional.isPresent()) throw new SwlcExportMarketException(404, "Promotion not found");
            Promotion promotion = promotionOptional.get();

            Optional<UserPromotion> userPromotion = userPromotionRepository.checkLiked(user, promotion);

            switch (promotionStatus) {
                case LIKE:
                    if(userPromotion.isPresent()) {
                        UserPromotion userPromotion1 = userPromotion.get();
                        userPromotion1.setLikeStatus(1);
                        userPromotionRepository.save(userPromotion1);
                    } else {
                        userPromotionRepository.save(new UserPromotion(user, promotion, 1));
                    }
                    break;
                case DISLIKE:
                    if(userPromotion.isPresent()) {
                        UserPromotion userPromotion1 = userPromotion.get();
                        userPromotion1.setLikeStatus(0);
                        userPromotionRepository.save(userPromotion1);
                    } else {
                        userPromotionRepository.save(new UserPromotion(user, promotion, 0));
                    }
                    break;
                default:
                    if(userPromotion.isPresent()) {
                        UserPromotion userPromotion1 = userPromotion.get();
                        userPromotionRepository.delete(userPromotion1);
                    } else {
                        throw new SwlcExportMarketException(404, "User promotion review not found");
                    }
                    break;
            }

        } catch (Exception e) {
            log.error("Execute method likePromotion: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void commentOnPromotion(int promotionId, String comment) {
        log.info("Execute method commentOnPromotion");
        try {

            User user = tokenValidator.retrieveUserInformationFromAuthentication();
            if(user == null) throw new SwlcExportMarketException(404, "User not found");

            Optional<Promotion> promotionOptional = promotionRepository.findById(promotionId);
            if(!promotionOptional.isPresent()) throw new SwlcExportMarketException(404, "Promotion not found");
            Promotion promotion = promotionOptional.get();

            userPromotionCommentRepository.save(new UserPromotionComment(user, promotion, new Date(), comment));

        } catch (Exception e) {
            log.error("Execute method commentOnPromotion: " + e.getMessage());
            throw e;
        }
    }

    PromotionDTO getPromotionDTO(Promotion promotion){
        return modelMapper.map(promotion,PromotionDTO.class);
    }

    PromotionUserResponseDTO getPromotionUserDTO(Promotion promotion){
        log.info("Execute method getPromotionUserDTO");
        try {

            PromotionUserResponseDTO promotionUserResponseDTO = modelMapper.map(promotion, PromotionUserResponseDTO.class);
            int userPromotionLikeCount = userPromotionRepository.getUserPromotionCount(promotion, 1);
            int userPromotionDislikeCount = userPromotionRepository.getUserPromotionCount(promotion, 0);
            User user = tokenValidator.retrieveUserInformationFromAuthentication();

            // like count
            promotionUserResponseDTO.setLikeCount(userPromotionLikeCount);

            // dislike count
            promotionUserResponseDTO.setDislikeCount(userPromotionDislikeCount);

            // user like
            if(user!=null) {
                Optional<UserPromotion> userPromotion = userPromotionRepository.checkLiked(user, promotion);
                promotionUserResponseDTO.setLoggedUser(true);
                if(userPromotion.isPresent()) {
                    promotionUserResponseDTO.setIsUserLiked(userPromotion.get().getLikeStatus()==1?1:2);
                }
            }

            // promotion comments
            List<UserPromotionComment> userPromotionCommentList = userPromotionCommentRepository.findByFkPromotion(promotion);

            List<PromotionCommentDTO> commentList = new ArrayList<>();

            for (UserPromotionComment userPromotionComment : userPromotionCommentList) {
                PromotionCommentDTO promotionCommentDTO = new PromotionCommentDTO();
                promotionCommentDTO.setName(userPromotionComment.getFkUser().getFirstName());
                promotionCommentDTO.setDate(userPromotionComment.getCreated_date());
                promotionCommentDTO.setComment(userPromotionComment.getComment());
                commentList.add(promotionCommentDTO);
            }

            promotionUserResponseDTO.setCommentList(commentList);

            return promotionUserResponseDTO;

        } catch (Exception e) {
            log.error("Execute method getPromotionUserDTO: " + e.getMessage());
            throw e;
        }
    }
}
