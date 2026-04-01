package com.example.lucaus26th.dto.response;

import com.example.lucaus26th.domain.food.FoodTruck;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FoodTruckResponseDto {
    private Long id;
    private Long settingId;
    private String name;
    private Long locationId;
    private String location;
    private String image;
    private String bestMenu;
    private Long likeCount;
    private boolean liked;
    private SettingResponseDto setting;

    public static FoodTruckResponseDto from(FoodTruck foodTruck, boolean liked) {
        return FoodTruckResponseDto.builder()
                .id(foodTruck.getId())
                .settingId(foodTruck.getSetting() != null ? foodTruck.getSetting().getId() : null)
                .name(foodTruck.getName())
                .locationId(foodTruck.getLocationId())
                .location(foodTruck.getLocation())
                .image(foodTruck.getImage())
                .bestMenu(foodTruck.getBestMenu())
                .likeCount(foodTruck.getLikeCount())
                .liked(liked)
                .setting(
                        foodTruck.getSetting() != null
                                ? SettingResponseDto.fromEntity(foodTruck.getSetting())
                                : null
                )
                .build();
    }
}