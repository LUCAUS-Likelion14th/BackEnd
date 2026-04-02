package com.example.lucaus26th.dto.request;


import com.example.lucaus26th.domain.booth.Booth;
import com.example.lucaus26th.enums.BoothLocation;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class BoothRequestDto {
    private Long locationId;
    // 운영정보
    private String name;
    private String owner;
    private String info;
    private BoothLocation location;
    //private long likeCount;

    private String image;
    private String locationImage;
    private String instagram;

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
