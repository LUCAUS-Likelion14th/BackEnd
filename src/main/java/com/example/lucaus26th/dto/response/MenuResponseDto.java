package com.example.lucaus26th.dto.response;

import com.example.lucaus26th.domain.food.Menu;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
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