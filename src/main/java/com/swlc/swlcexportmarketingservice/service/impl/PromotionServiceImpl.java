package com.swlc.swlcexportmarketingservice.service.impl;

import com.swlc.swlcexportmarketingservice.dto.PromotionDTO;
import com.swlc.swlcexportmarketingservice.entity.Promotion;
import com.swlc.swlcexportmarketingservice.enums.PromotionStatus;
import com.swlc.swlcexportmarketingservice.exception.SwlcExportMarketException;
import com.swlc.swlcexportmarketingservice.repository.PromotionRepository;
import com.swlc.swlcexportmarketingservice.service.PromotionService;
import com.swlc.swlcexportmarketingservice.util.FileHandler;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.swlc.swlcexportmarketingservice.constant.ApplicationConstant.NOT_FOUND_PROMOTION;

@Service
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final FileHandler fileHandler;
    private final ModelMapper modelMapper;

    public PromotionServiceImpl(PromotionRepository promotionRepository, FileHandler fileHandler, ModelMapper modelMapper) {
        this.promotionRepository = promotionRepository;
        this.fileHandler = fileHandler;
        this.modelMapper = modelMapper;
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
    public Page<PromotionDTO> getAllPromotions(Pageable pageable) {
        return promotionRepository.getAllPromotions(pageable).map(this::getPromotionDTO);
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

    PromotionDTO getPromotionDTO(Promotion promotion){
        return modelMapper.map(promotion,PromotionDTO.class);
    }
}
