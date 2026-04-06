package com.example.lucaus26th.controller;

import com.example.lucaus26th.dto.response.MyPageResponseDto;
import com.example.lucaus26th.dto.response.booth.BoothResponseDto;
import com.example.lucaus26th.dto.response.foodTruck.FoodTruckResponseDto;
import com.example.lucaus26th.security.CustomUserDetails;
import com.example.lucaus26th.service.MyPageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Tag(name = "마이페이지", description = "마이페이지에 사용할 API입니다.")
public class MyPageController {

    private final MyPageService myPageService;

    // 마이페이지 홈 조회
    @GetMapping
    public ResponseEntity<MyPageResponseDto> getMyPage(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails user)) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        return ResponseEntity.ok(myPageService.getMyPage(user));
    }

    // 내가 좋아요 한 부스
    @GetMapping("/booth")
    public ResponseEntity<List<BoothResponseDto.Lists>> getMyBoothLikes(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<BoothResponseDto.Lists> response =  myPageService.getMyBoothLikes(userDetails);
        return ResponseEntity.ok(response);
    }


    // 내가 좋아요 한 푸드트럭
    @GetMapping("/foodtruck")
    public ResponseEntity<List<FoodTruckResponseDto>> getMyFoodTruckLikes(Authentication authentication)
    {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails user)) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        return ResponseEntity.ok(myPageService.getMyFoodTruckLikes(user));
    }
}
