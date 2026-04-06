package com.example.lucaus26th.dto.response;

import com.example.lucaus26th.domain.Member;
import com.example.lucaus26th.dto.response.booth.BoothResponseDto;
import com.example.lucaus26th.dto.response.food.MyFoodTruckResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MyPageResponseDto {

    private String name;
    @JsonProperty("like_count")
    private int likeCount;
    @JsonProperty("booth_like_list")
    private List<BoothResponseDto.MyBooth> boothLikeList;
    // 여기에 푸드 관련 추가해야할것. private List<FoodTruckResponseDto> ~
    @JsonProperty("food_truck_like_list")
    private List<MyFoodTruckResponseDto> foodTruckLikeList;
    // 나중에는 private int stampCount; 추가하기

    public static MyPageResponseDto fromEntity(
            Member member,
            int boothLikeCount,
            List<BoothResponseDto.MyBooth> boothLikeList,
            int foodTruckLikeCount,
            List<MyFoodTruckResponseDto> foodTruckLikeList){ //나중에 푸드도

        return MyPageResponseDto.builder()
                .name(member.getName())
                .likeCount(boothLikeCount+foodTruckLikeCount) // 여기에 푸드트럭 리스트 사이즈도 더하기
                .boothLikeList(boothLikeList)
                .foodTruckLikeList(foodTruckLikeList)
                .build();
    }

}
