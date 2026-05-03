package com.example.lucaus26th.dto.response.foodTruck;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FoodTruckDetailResponseDto {

    private Long id;
    private String name;
    private Long locationId;
    private String location;
    private String image;
    private String bestMenu;
    private Long likeCount;
    private boolean liked;
    private String foodTruckInfo;

    private List<String> date;
    private List<MenuDto> menu;

    @Getter
    @Builder
    public static class MenuDto {
        private String name;
        private Long price;
        private String image;
    }

    public FoodTruckDetailResponseDto withLiked(boolean liked) {
        return FoodTruckDetailResponseDto.builder()
                .id(this.id)
                .name(this.name)
                .locationId(this.locationId)
                .location(this.location)
                .image(this.image)
                .bestMenu(this.bestMenu)
                .likeCount(this.likeCount)
                .liked(liked)
                .foodTruckInfo(this.foodTruckInfo)
                .date(this.date)
                .menu(this.menu)
                .build();
    }
}
