package com.example.lucaus26th.dto.response.booth;


import com.example.lucaus26th.domain.booth.Booth;
import com.example.lucaus26th.enums.BoothLocation;
import lombok.Builder;
import lombok.Getter;
import java.util.List;


public class BoothResponseDto {

    @Getter
    @Builder
    public static class Lists { // 전체조회
        private Long booth_id;
        private Long location_id;
        private String booth_image;
        private String booth_name;
        private String booth_location;
        private Boolean is_liked;
        private Long like_count;

        public static BoothResponseDto.Lists fromEntity(Booth booth, boolean isLiked){
            return Lists.builder()
                    .booth_id(booth.getId())
                    .location_id(booth.getLocationId())
                    .booth_image(booth.getImage())
                    .booth_name(booth.getName())
                    .booth_location(booth.getLocation().getDescription())
                    .is_liked(isLiked)
                    .like_count(booth.getLikeCount())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class Detail { // 상세조회
        private Long booth_id;
        private Long location_id;
        private String booth_image;
        private String booth_name;
        private List<String> booth_category;  // ["소개팅", "음식"]
        private String booth_info;
        private String owner_insta;
        private Boolean is_liked;
        private Long like_count;
        private String location;
        private List<String> date;
        private String location_image;

        public static BoothResponseDto.Detail fromEntity(Booth booth, boolean isLiked){
            return Detail.builder()
                    .booth_id(booth.getId())
                    .location_id(booth.getLocationId())
                    .booth_image(booth.getImage())
                    .booth_name(booth.getName())
                    .booth_category(booth.getCategoryNames())// 카테고리
                    .booth_info(booth.getInfo())
                    .owner_insta(booth.getInstagram())
                    .is_liked(isLiked)
                    .like_count(booth.getLikeCount())
                    .location(booth.getLocation().getDescription())
                    .date(booth.getDays())
                    .location_image(booth.getLocationImage())
                    .build();
        }

    }

    @Getter
    @Builder
    public static class Hot{
        private Long booth_id;
        private Long location_id;
        private String booth_image;
        private String location;
        private String booth_name;
        private String owner;
        private Long like_count;
        private Boolean is_liked;

        public static BoothResponseDto.Hot fromEntity(Booth booth, boolean isLiked){
            return Hot.builder()
                    .booth_id(booth.getId())
                    .location_id(booth.getLocationId())
                    .booth_image(booth.getImage())
                    .location(booth.getLocation().getDescription())
                    .booth_name(booth.getName())
                    .is_liked(isLiked)
                    .like_count(booth.getLikeCount())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class MyBooth{
        private Long booth_id;
        private Long location_id;
        private String booth_image;
        private String booth_name;

        public static BoothResponseDto.MyBooth fromEntity(Booth booth){
            return MyBooth.builder()
                    .booth_id(booth.getId())
                    .location_id(booth.getLocationId())
                    .booth_image(booth.getImage())
                    .booth_name(booth.getName())
                    .build();
        }
    }
}
