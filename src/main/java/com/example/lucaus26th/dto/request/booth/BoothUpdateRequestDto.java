package com.example.lucaus26th.dto.request.booth;

import com.example.lucaus26th.enums.BoothLocation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class BoothUpdateRequestDto {

    private Long locationId;
    private String name;
    private String owner;
    private String info;
    private BoothLocation location;
    @Schema(type = "string", format = "binary", description = "부스 이미지 파일")
    private MultipartFile image;
    @Schema(type = "string", format = "binary", description = "부스위치 이미지 파일")
    private MultipartFile locationImage;
    private String instagram;
    private String stampPwd;
    //private SettingRequest setting;

    /*@Getter
    @NoArgsConstructor
    public static class SettingRequest {
        private String mon;
        private String tue;
        private String wed;
        private String thu;
        private String fri;
    }*/

}
