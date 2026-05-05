package com.example.lucaus26th.dto.response.foodTruck;

import com.example.lucaus26th.domain.foodTruck.FoodTruck;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public FoodTruckResponseDto withLiked(boolean liked) {
        return FoodTruckResponseDto.builder()
                .id(this.id)
                .name(this.name)
                .locationId(this.locationId)
                .location(this.location)
                .image(this.image)
                .bestMenu(this.bestMenu)
                .likeCount(this.likeCount)
                .liked(liked)
                .build();
    }
}