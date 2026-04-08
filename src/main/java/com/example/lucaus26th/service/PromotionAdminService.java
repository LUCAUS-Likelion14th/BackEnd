package com.example.lucaus26th.service;

import com.example.lucaus26th.domain.Promotion;
import com.example.lucaus26th.dto.request.PromotionRequestDTO;
import com.example.lucaus26th.dto.response.PromotionResponseDTO;
import com.example.lucaus26th.global.S3Service;
import com.example.lucaus26th.global.exception.BusinessException;
import com.example.lucaus26th.global.exception.ErrorCode;
import com.example.lucaus26th.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PromotionAdminService {
    private final PromotionRepository promotionRespository;
    private final S3Service s3Service;

    @Transactional
    public PromotionResponseDTO createPromotion(PromotionRequestDTO request) {
        String imageUrl;
        try{
            imageUrl = s3Service.upload(request.getImage(), "promotion");
        } catch(IOException e){
            throw new RuntimeException("S3 이미지 업로드에 실패했습니다.", e);
        }
        Promotion promotion = Promotion.create(imageUrl, request.getInstagram());
        promotionRespository.save(promotion);
        return PromotionResponseDTO.from(promotion);
    }

    @Transactional
    public PromotionResponseDTO updatePromotion(Long promoId, PromotionRequestDTO request) {
        Promotion promotion = promotionRespository.findById(promoId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROMOTION_NOT_FOUND));

        String imageUrl;
        try{
            imageUrl = s3Service.upload(request.getImage(), "promotion");
        } catch (IOException e) {
            throw new RuntimeException("S3 이미지 업로드에 실패했습니다.", e);
        }
        promotion.updatePromotion(imageUrl, request.getInstagram());
        return PromotionResponseDTO.from(promotion);
    }

    @Transactional
    public void deletePromotion(Long promoId) {
        Promotion promotion = promotionRespository.findById(promoId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROMOTION_NOT_FOUND));
        promotionRespository.delete(promotion);
    }
}
