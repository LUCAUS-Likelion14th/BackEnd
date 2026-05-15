package com.example.lucaus26th.dto.request.booth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoothRequestDto {
    // 운영정보
    private String name;
    private String owner;
    private String info;
    //private long likeCount;

    @Schema(type = "string", format = "binary", description = "부스 이미지 파일")
    private MultipartFile image;
    private String instagram;
    private String stampPwd;

    //private SettingRequest setting;
    private List<Long> categoryIds = new ArrayList<>();


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
