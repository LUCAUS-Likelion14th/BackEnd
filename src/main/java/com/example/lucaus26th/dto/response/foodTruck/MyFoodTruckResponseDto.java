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
