package com.example.lucaus26th.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class FoodTruckSettingRequestDto {
    private LocalDate date;
    private String day;
    private LocalTime startAt;
    private LocalTime endAt;
}