package com.example.lucaus26th.dto.response;

import com.example.lucaus26th.domain.food.FoodTruckSetting;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class FoodTruckSettingResponseDto {
    private Long id;
    private Long foodTruckId;
    private LocalDate date;
    private String day;
    private LocalTime startAt;
    private LocalTime endAt;

    public static FoodTruckSettingResponseDto from(FoodTruckSetting setting) {
        return FoodTruckSettingResponseDto.builder()
                .id(setting.getId())
                .foodTruckId(setting.getFoodTruck().getId())
                .date(setting.getDate())
                .day(setting.getDay())
                .startAt(setting.getStartAt())
                .endAt(setting.getEndAt())
                .build();
    }
}