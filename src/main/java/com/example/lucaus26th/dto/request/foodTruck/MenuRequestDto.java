package com.example.lucaus26th.dto.request.foodTruck;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuRequestDto {

    private String name;
    private Long price;
    private String image;
}