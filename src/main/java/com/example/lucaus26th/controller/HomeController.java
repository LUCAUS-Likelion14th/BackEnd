package com.example.lucaus26th.controller;

import com.example.lucaus26th.dto.response.food.HotFoodTruckResponseDto;
import com.example.lucaus26th.dto.response.stage.LiveStageResponseDTO;
import com.example.lucaus26th.global.api.ApiResponse;
import com.example.lucaus26th.security.CustomUserDetails;
import com.example.lucaus26th.service.food.FoodTruckService;
import com.example.lucaus26th.service.stage.StageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "메인 홈", description = "메인 홈에 사용할 API입니다.")
public class HomeController {
    private final StageService stageService;
    private final FoodTruckService foodTruckService;

    @GetMapping("/live-stage")
    @Operation(summary = "실시간 공연 조회", description = "메인홈에서 현재 진행 중인 공연을 조회합니다.")
    public ResponseEntity<ApiResponse<LiveStageResponseDTO>> getLiveStage() {
        return ResponseEntity.ok(ApiResponse.success(stageService.getLiveStage()));
    }

    @GetMapping("/hot-food")
    public ResponseEntity<List<HotFoodTruckResponseDto>> getHotFoodTrucks(Authentication authentication) {
        Long memberId = null;
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails user) {
            memberId = user.getId();
        }

        return ResponseEntity.ok(foodTruckService.getHotFoodTrucks(memberId));
    }

}
