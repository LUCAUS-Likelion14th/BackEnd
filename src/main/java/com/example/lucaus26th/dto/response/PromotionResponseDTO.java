package com.example.lucaus26th.dto.response;

import com.example.lucaus26th.domain.Promotion;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PromotionResponseDTO {
    private Long id;
    private String image;
    private String instagram;

    public static PromotionResponseDTO from(Promotion promotion){
        return PromotionResponseDTO.builder()
                .id(promotion.getId())
                .image(promotion.getImage())
                .instagram(promotion.getInstagram())
                .build();
    }
}
