package com.example.lucaus26th.dto.response;

import com.example.lucaus26th.domain.food.FoodTruck;
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
}
