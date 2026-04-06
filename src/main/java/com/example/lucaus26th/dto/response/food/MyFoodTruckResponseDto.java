package com.example.lucaus26th.dto.response.food;

import com.example.lucaus26th.domain.food.FoodTruck;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyFoodTruckResponseDto {
    private Long id;
    private String name;
    private String image;

    public static MyFoodTruckResponseDto from(FoodTruck foodTruck) {
        return MyFoodTruckResponseDto.builder()
                .id(foodTruck.getId())
                .name(foodTruck.getName())
                .image(foodTruck.getImage())
                .build();
    }
}
