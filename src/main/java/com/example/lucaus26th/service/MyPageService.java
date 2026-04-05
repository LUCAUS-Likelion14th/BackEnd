package com.example.lucaus26th.service;


import com.example.lucaus26th.domain.Member;
import com.example.lucaus26th.domain.booth.BoothLike;
import com.example.lucaus26th.dto.response.MyPageResponseDto;
import com.example.lucaus26th.dto.response.booth.BoothResponseDto;
import com.example.lucaus26th.repository.booth.BoothLikeRepository;
import com.example.lucaus26th.repository.booth.BoothRepository;
import com.example.lucaus26th.repository.food.FoodLikeRepository;
import com.example.lucaus26th.repository.food.FoodTruckRepository;
import com.example.lucaus26th.security.CustomUserDetails;
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
    private final FoodLikeRepository foodLikeRepository;
    private final FoodTruckRepository foodTruckRepository; 
    //아니 왜 뭐는 푸드트럭이고 왜 뭐는 푸드에요 넘 헷갈링댕 ㅜ
    // 나중에 도장판 개수 알려주는거 추가하는거 잊지말기

    // 마이페이지 전체 조회
    public MyPageResponseDto getMyPage(CustomUserDetails userDetails){
        Member member = userDetails.getMember(); // 이거 무조건 member(로그인) 필요하니까, securityconfig였나 거기에 url추가하기 (좋아요처럼)

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


        // 스탬프 관련


        return MyPageResponseDto.fromEntity(member,boothLikeCount, boothLikeList);
    }

    // 내 좋아요 조회 - 부스

    // 내 좋아요 조회 - 푸드트럭

}
