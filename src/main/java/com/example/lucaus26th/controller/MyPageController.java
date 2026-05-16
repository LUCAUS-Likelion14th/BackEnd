package com.example.lucaus26th.controller;

import com.example.lucaus26th.domain.Member;
import com.example.lucaus26th.dto.response.MyPageResponseDto;
import com.example.lucaus26th.dto.response.booth.BoothResponseDto;
import com.example.lucaus26th.dto.response.foodTruck.FoodTruckResponseDto;
import com.example.lucaus26th.global.api.ApiResponse;
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
    public ResponseEntity<ApiResponse<MyPageResponseDto>> getMyPage(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails user)) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        Member member = user.getMember();

        return ResponseEntity.ok(ApiResponse.success(myPageService.getMyPage(member)));
    }

    // 내가 좋아요 한 부스
    @GetMapping("/booth")
    public ResponseEntity<ApiResponse<List<BoothResponseDto.MyBooth>>> getMyBoothLikes(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member member =
                (userDetails != null)
                        ? userDetails.getMember()
                        : null;

        List<BoothResponseDto.MyBooth> response =
                myPageService.getMyBoothLikes(member);

        return ResponseEntity.ok(ApiResponse.success(response));
    }


    // 내가 좋아요 한 푸드트럭
    @GetMapping("/foodtruck")
    public ResponseEntity<ApiResponse<List<FoodTruckResponseDto>>> getMyFoodTruckLikes(Authentication authentication)
    {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails user)) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        Member member = user.getMember();

        return ResponseEntity.ok(ApiResponse.success(myPageService.getMyFoodTruckLikes(member)));
    }
}
