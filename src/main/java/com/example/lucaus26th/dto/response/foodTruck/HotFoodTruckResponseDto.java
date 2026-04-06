package com.example.lucaus26th.dto.response.foodTruck;

import com.example.lucaus26th.domain.foodTruck.FoodTruck;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HotFoodTruckResponseDto {
    private Long id;
    private String name;
    private String image;
    private String bestMenu;
    private Long likeCount;
    private boolean liked;

    public static HotFoodTruckResponseDto from(FoodTruck foodTruck, boolean liked) {
        return HotFoodTruckResponseDto.builder()
                .id(foodTruck.getId())
                .name(foodTruck.getName())
                .image(foodTruck.getImage())
                .bestMenu(foodTruck.getBestMenu())
                .likeCount(foodTruck.getLikeCount())
                .liked(liked)
                .build();
    }
}