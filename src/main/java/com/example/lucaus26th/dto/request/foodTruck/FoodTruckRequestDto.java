package com.example.lucaus26th.dto.request.foodTruck;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class FoodTruckRequestDto {
    private String name;
    private Long locationId;
    private String location;

    @Schema(type = "string", format = "binary", description = "푸드트럭 이미지 파일")
    private MultipartFile image;

    private String bestMenu;
    private String foodTruckInfo;
}