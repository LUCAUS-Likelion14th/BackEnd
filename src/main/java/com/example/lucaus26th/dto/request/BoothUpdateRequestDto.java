package com.example.lucaus26th.dto.request;

import com.example.lucaus26th.enums.BoothLocation;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoothUpdateRequestDto {

    private Long locationId;
    private String name;
    private String owner;
    private String info;
    private BoothLocation location;
    private String image;
    private String locationImage;
    private String instagram;
    private SettingRequest setting;

    @Getter
    @NoArgsConstructor
    public static class SettingRequest {
        private String mon;
        private String tue;
        private String wed;
        private String thu;
        private String fri;
    }

}
