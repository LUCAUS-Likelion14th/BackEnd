package com.example.lucaus26th.dto.request.foodTruck;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class MenuRequestDto {

    private String name;
    private Long price;

    @Schema(type = "string", format = "binary", description = "메뉴 이미지 파일")
    private MultipartFile image;
}