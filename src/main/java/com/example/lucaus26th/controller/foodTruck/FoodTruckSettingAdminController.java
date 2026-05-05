package com.example.lucaus26th.controller.foodTruck;

import com.example.lucaus26th.dto.request.foodTruck.FoodTruckSettingRequestDto;
import com.example.lucaus26th.dto.response.foodTruck.FoodTruckSettingResponseDto;
import com.example.lucaus26th.global.api.ApiResponse;
import com.example.lucaus26th.service.foodTruck.FoodTruckService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/foodtruck/{foodTruckId}/setting")
@RequiredArgsConstructor
@Tag(name = "푸드트럭 운영정보 관리", description = "푸드트럭 운영정보 등록/수정/삭제 관련 API")
public class FoodTruckSettingAdminController {

    private final FoodTruckService foodTruckService;

    @PostMapping
    public ResponseEntity<ApiResponse<FoodTruckSettingResponseDto>> createSetting(
            @PathVariable Long foodTruckId,
            @RequestBody FoodTruckSettingRequestDto dto
    ) {
        FoodTruckSettingResponseDto response = foodTruckService.createSetting(foodTruckId, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    @PatchMapping("/{settingId}")
    public ResponseEntity<ApiResponse<FoodTruckSettingResponseDto>> updateSetting(
            @PathVariable Long foodTruckId,
            @PathVariable Long settingId,
            @RequestBody FoodTruckSettingRequestDto dto
    ) {
        FoodTruckSettingResponseDto response =
                foodTruckService.updateSetting(foodTruckId, settingId, dto);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{settingId}")
    public ResponseEntity<ApiResponse<Void>> deleteSetting(
            @PathVariable Long foodTruckId,
            @PathVariable Long settingId
    ) {
        foodTruckService.deleteSetting(foodTruckId, settingId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}