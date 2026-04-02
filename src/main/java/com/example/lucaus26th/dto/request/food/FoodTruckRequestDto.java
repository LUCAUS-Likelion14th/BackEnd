package com.example.lucaus26th.dto.request.food;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FoodTruckRequestDto {
    private String name;
    private Long locationId;
    private String location;
    private String image;
    private String bestMenu;
    private String foodTruckInfo;
}