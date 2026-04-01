package com.example.lucaus26th.dto.response;

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
}