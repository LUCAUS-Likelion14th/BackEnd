package com.example.lucaus26th.controller.foodTruck;

import com.example.lucaus26th.dto.response.foodTruck.FoodTruckDetailResponseDto;
import com.example.lucaus26th.dto.response.foodTruck.FoodTruckResponseDto;
import com.example.lucaus26th.global.api.ApiResponse;
import com.example.lucaus26th.global.exception.BusinessException;
import com.example.lucaus26th.global.exception.ErrorCode;
import com.example.lucaus26th.security.CustomUserDetails;
import com.example.lucaus26th.service.foodTruck.FoodTruckService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ApiResponse<Void>> createFoodLike(
            @PathVariable Long foodTruckId,
            Authentication authentication
    ) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails user)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        foodTruckService.createFoodLike(foodTruckId, user.getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{foodTruckId}/like")
    public ResponseEntity<ApiResponse<Void>> deleteFoodLike(
            @PathVariable Long foodTruckId,
            Authentication authentication
    ) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails user)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        foodTruckService.deleteFoodLike(foodTruckId, user.getId());

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FoodTruckResponseDto>>> getFoodTrucks(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String date,
            Authentication authentication
    ) {
        Long memberId = null;

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails user) {
            memberId = user.getId();
        }

        return ResponseEntity.ok(ApiResponse.success(foodTruckService.getFoodTrucks(location, date, memberId)));
    }

    @GetMapping("/{foodTruckId}")
    public ResponseEntity<ApiResponse<FoodTruckDetailResponseDto>> getFoodTruckDetail(
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

        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
