package com.example.lucaus26th.service;


import com.example.lucaus26th.domain.Member;
import com.example.lucaus26th.domain.booth.Booth;
import com.example.lucaus26th.domain.booth.BoothLike;
import com.example.lucaus26th.domain.booth.BoothSetting;
import com.example.lucaus26th.domain.foodTruck.FoodTruckLike;
import com.example.lucaus26th.dto.response.MyPageResponseDto;
import com.example.lucaus26th.dto.response.booth.BoothResponseDto;
import com.example.lucaus26th.dto.response.foodTruck.FoodTruckResponseDto;
import com.example.lucaus26th.dto.response.foodTruck.MyFoodTruckResponseDto;
import com.example.lucaus26th.repository.booth.BoothLikeRepository;
import com.example.lucaus26th.repository.booth.BoothRepository;
import com.example.lucaus26th.repository.foodTruck.FoodTruckLikeRepository;
import com.example.lucaus26th.repository.foodTruck.FoodTruckRepository;
import com.example.lucaus26th.service.booth.BoothCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPageService {

    // 이름은 ? CustomUser userDetail에서 가져오면 되고
    private final BoothLikeRepository boothLikeRepository;
    private final BoothRepository boothRepository;
    private final FoodTruckLikeRepository foodTruckLikeRepository;
    private final FoodTruckRepository foodTruckRepository;
    private final BoothCacheService boothCacheService;
    //아니 왜 뭐는 푸드트럭이고 왜 뭐는 푸드에요 넘 헷갈링댕 ㅜ
    // 나중에 도장판 개수 알려주는거 추가하는거 잊지말기

    // 마이페이지 전체 조회
    public MyPageResponseDto getMyPage(Member member) {
        //Member member = userDetails.getMember(); // 이거 무조건 member(로그인) 필요하니까, securityconfig였나 거기에 url추가하기 (좋아요처럼)

        // 부스관련
        List<BoothLike> allBoothLikes = boothLikeRepository.findByMember(member);
        int boothLikeCount = allBoothLikes.size();
        // (id 큰순으로, 즉 최신순) 최근 3개
        List<BoothResponseDto.MyBooth> boothLikeList = allBoothLikes.stream()
                .sorted(Comparator.comparing(BoothLike::getId).reversed())
                .limit(3)
                .map(boothLike -> BoothResponseDto.MyBooth.fromEntity(boothLike.getBooth()))
                .toList();

        // 푸드트럭 관련
        List<FoodTruckLike> allFoodLikes = foodTruckLikeRepository.findByMember(member);
        int foodTruckLikeCount = allFoodLikes.size();

        List<MyFoodTruckResponseDto> foodTruckLikeList = allFoodLikes.stream()
                .sorted(Comparator.comparing(FoodTruckLike::getId).reversed())
                .limit(3)
                .map(foodTruckLike -> MyFoodTruckResponseDto.from(foodTruckLike.getFoodTruck()))
                .toList();


        // 스탬프 관련

        return MyPageResponseDto.fromEntity(
                member,
                boothLikeCount,
                boothLikeList,
                foodTruckLikeCount,
                foodTruckLikeList
        );
    }


    // 내 좋아요 조회 - 부스
    public List<BoothResponseDto.Lists> getMyBoothLikes(Member member) {
        List<BoothLike> boothLikes = boothLikeRepository.findByMember(member);
        return boothLikes.stream()
                .sorted(Comparator.comparing(BoothLike::getId).reversed())
                .map(boothLike -> {
                    Booth booth = boothLike.getBooth();
                    BoothSetting setting = boothCacheService.getDisplaySetting(booth, null);
                    return BoothResponseDto.Lists.fromEntity(booth, setting, true);
                })
                .toList();
    }

    // 내 좋아요 조회 - 푸드트럭
    public List<FoodTruckResponseDto> getMyFoodTruckLikes(Member member) {
        //Member member = userDetails.getMember();

        List<FoodTruckLike> foodTruckLikes = foodTruckLikeRepository.findByMember(member);

        return foodTruckLikes.stream()
                .sorted(Comparator.comparing(FoodTruckLike::getId).reversed())
                .map(foodTruckLike -> FoodTruckResponseDto.from(foodTruckLike.getFoodTruck(), true))
                .toList();
    }

}
