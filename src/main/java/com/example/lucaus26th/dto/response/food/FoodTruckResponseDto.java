package com.example.lucaus26th.dto.response.food;

import com.example.lucaus26th.domain.food.FoodTruck;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FoodTruckResponseDto {
    private Long id;
    private String name;
    private Long locationId;
    private String location;
    private String image;
    private String bestMenu;
    private Long likeCount;
    private boolean liked;

    public static FoodTruckResponseDto from(FoodTruck foodTruck, boolean liked) {
        return FoodTruckResponseDto.builder()
                .id(foodTruck.getId())
                .name(foodTruck.getName())
                .locationId(foodTruck.getLocationId())
                .location(foodTruck.getLocation())
                .image(foodTruck.getImage())
                .bestMenu(foodTruck.getBestMenu())
                .likeCount(foodTruck.getLikeCount())
                .liked(liked)
                .build();
    }
}