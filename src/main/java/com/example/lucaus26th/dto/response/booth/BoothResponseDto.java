package com.example.lucaus26th.dto.response.booth;


import com.example.lucaus26th.domain.booth.Booth;
import com.example.lucaus26th.domain.booth.BoothSetting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Comparator;


public class BoothResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Lists { // 전체조회
        private Long booth_id;
        private String location_id;
        private String location;
        private String booth_image;
        private String booth_name;
        private String booth_owner;
        private Boolean is_liked;
        private Long like_count;

        public static BoothResponseDto.Lists fromEntity(Booth booth, BoothSetting setting, boolean isLiked){
            return Lists.builder()
                    .booth_id(booth.getId())
                    .location_id(setting != null ? setting.getLocationId() : null)
                    .location(
                            setting != null && setting.getLocation() != null
                                    ? setting.getLocation().getDescription()
                                    : null
                    )
                    .booth_image(booth.getImage())
                    .booth_name(booth.getName())
                    .booth_owner(booth.getOwner())
                    .is_liked(isLiked)
                    .like_count(booth.getLikeCount())
                    .build();
        }
        public BoothResponseDto.Lists withLiked(boolean isLiked) {

            return Lists.builder()
                    .booth_id(this.booth_id)
                    .location_id(this.location_id)
                    .location(this.location)
                    .booth_image(this.booth_image)
                    .booth_name(this.booth_name)
                    .booth_owner(this.booth_owner)
                    .is_liked(isLiked)
                    .like_count(this.like_count)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail { // 상세조회
        private Long booth_id;
        private String booth_image;
        private String booth_name;
        private List<String> booth_category;  // ["소개팅", "음식"]
        private String booth_info;
        private String owner_insta;
        private Boolean is_liked;
        private Long like_count;
        private List<BoothSettingResponseDto> settings;

        public static BoothResponseDto.Detail fromEntity(Booth booth, boolean isLiked){
            return Detail.builder()
                    .booth_id(booth.getId())
                    .booth_image(booth.getImage())
                    .booth_name(booth.getName())
                    .booth_category(booth.getCategoryNames())// 카테고리
                    .booth_info(booth.getInfo())
                    .owner_insta(booth.getInstagram())
                    .is_liked(isLiked)
                    .like_count(booth.getLikeCount())
                    .settings(
                            booth.getSettings().stream()
                                    .sorted(Comparator.comparing(BoothSetting::getDate))
                                    .map(BoothSettingResponseDto::fromEntity)
                                    .toList()
                    )
                    .build();
        }

        public BoothResponseDto.Detail withLiked(boolean isLiked) {
            return Detail.builder()
                    .booth_id(this.booth_id)
                    .booth_image(this.booth_image)
                    .booth_name(this.booth_name)
                    .booth_category(this.booth_category)
                    .booth_info(this.booth_info)
                    .owner_insta(this.owner_insta)
                    .is_liked(isLiked)
                    .like_count(this.like_count)
                    .settings(this.settings)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Hot{
        private Long booth_id;
        private String location_id;
        private String location;
        private String booth_image;
        private String booth_name;
        private String owner;
        private Long like_count;
        private Boolean is_liked;

        public static BoothResponseDto.Hot fromEntity(Booth booth, BoothSetting setting, boolean isLiked){
            return Hot.builder()
                    .booth_id(booth.getId())
                    .location_id(setting != null ? setting.getLocationId() : null)
                    .location(
                            setting != null && setting.getLocation() != null
                                    ? setting.getLocation().getDescription()
                                    : null
                    )
                    .booth_image(booth.getImage())
                    .booth_name(booth.getName())
                    .owner(booth.getOwner())
                    .is_liked(isLiked)
                    .like_count(booth.getLikeCount())
                    .build();
        }

        public BoothResponseDto.Hot withLiked(boolean isLiked) {
            return Hot.builder()
                    .booth_id(this.booth_id)
                    .location_id(this.location_id)
                    .booth_image(this.booth_image)
                    .booth_name(this.booth_name)
                    .owner(this.owner)
                    .like_count(this.like_count)
                    .is_liked(isLiked)
                    .location(this.location)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyBooth{
        private Long booth_id;
        private String booth_image;
        private String booth_name;
        private Long likeCount;
        private boolean liked;

        public static BoothResponseDto.MyBooth fromEntity(Booth booth, boolean isLiked){
            return MyBooth.builder()
                    .booth_id(booth.getId())
                    .booth_image(booth.getImage())
                    .booth_name(booth.getName())
                    .likeCount(booth.getLikeCount())
                    .liked(isLiked)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class All { // create, update 할때만
        private Long booth_id;
        private String booth_image;
        private String booth_name;
        private String booth_owner;
        private List<String> booth_category;  // ["소개팅", "음식"]
        private String booth_info;
        private String owner_insta;
        private Long like_count;
        //private List<String> date;

        public static BoothResponseDto.All fromEntity(Booth booth){
            return All.builder()
                    .booth_id(booth.getId())
                    .booth_image(booth.getImage())
                    .booth_name(booth.getName())
                    .booth_owner(booth.getOwner())
                    .booth_category(booth.getCategoryNames())// 카테고리
                    .booth_info(booth.getInfo())
                    .owner_insta(booth.getInstagram())
                    .like_count(booth.getLikeCount())
                    //.date(booth.getDays())
                    .build();
        }

    }
}
