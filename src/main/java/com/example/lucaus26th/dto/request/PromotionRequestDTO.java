package com.example.lucaus26th.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class PromotionRequestDTO {

    @Schema(type = "string", format = "binary", description = "프로모션 이미지 파일")
    private MultipartFile image;

    private String instagram;
}
