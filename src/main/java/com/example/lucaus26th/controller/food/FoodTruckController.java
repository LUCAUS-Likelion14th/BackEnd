package com.example.lucaus26th.controller.food;

import com.example.lucaus26th.dto.request.food.FoodTruckRequestDto;
import com.example.lucaus26th.dto.request.food.FoodTruckSettingRequestDto;
import com.example.lucaus26th.dto.request.food.MenuRequestDto;
import com.example.lucaus26th.dto.response.food.FoodTruckDetailResponseDto;
import com.example.lucaus26th.dto.response.food.FoodTruckResponseDto;
import com.example.lucaus26th.dto.response.food.FoodTruckSettingResponseDto;
import com.example.lucaus26th.dto.response.food.MenuResponseDto;
import com.example.lucaus26th.security.CustomUserDetails;
import com.example.lucaus26th.service.food.FoodTruckService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/foodtruck")
@RequiredArgsConstructor
@Tag(name = "푸드트럭", description = "푸드트럭 관련 API")
public class FoodTruckController {
    private final FoodTruckService foodTruckService;

    @PostMapping("/{foodTruckId}/like")
    public ResponseEntity<String> createFoodLike(
            @PathVariable Long foodTruckId,
            Authentication authentication
    ) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails user)) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        foodTruckService.createFoodLike(foodTruckId, user.getId());
        return ResponseEntity.ok("푸드트럭 좋아요 성공");
    }

    @DeleteMapping("/{foodTruckId}/like")
    public ResponseEntity<Void> deleteFoodLike(
            @PathVariable Long foodTruckId,
            Authentication authentication
    ) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails user)) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        foodTruckService.deleteFoodLike(foodTruckId, user.getId());

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FoodTruckResponseDto>> getFoodTrucks(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String date,
            Authentication authentication
    ) {
        Long memberId = null;

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails user) {
            memberId = user.getId();
        }

        return ResponseEntity.ok(foodTruckService.getFoodTrucks(location, date, memberId));
    }

    @GetMapping("/{foodTruckId}")
    public ResponseEntity<FoodTruckDetailResponseDto> getFoodTruckDetail(
            @PathVariable Long foodTruckId,
            Authentication authentication
    ) {
        Long memberId = null;

        // 로그인 유저 있으면 memberId 추출
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails user) {
            memberId = user.getId();
        }

        FoodTruckDetailResponseDto response =
                foodTruckService.getFoodTruckDetail(foodTruckId, memberId);

        return ResponseEntity.ok(response);
    }

}
