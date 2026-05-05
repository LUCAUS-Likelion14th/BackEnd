package com.example.lucaus26th.dto.response.foodTruck;

import com.example.lucaus26th.domain.foodTruck.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuResponseDto {

    private Long menuId;
    private String name;
    private Long price;
    private String image;

    public static MenuResponseDto from(Menu menu) {
        return MenuResponseDto.builder()
                .menuId(menu.getId())
                .name(menu.getName())
                .price(menu.getPrice())
                .image(menu.getImage())
                .build();
    }
}