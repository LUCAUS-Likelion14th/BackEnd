package com.example.lucaus26th.controller.foodTruck;

import com.example.lucaus26th.dto.request.foodTruck.FoodTruckRequestDto;
import com.example.lucaus26th.dto.request.foodTruck.MenuRequestDto;
import com.example.lucaus26th.dto.response.foodTruck.FoodTruckResponseDto;
import com.example.lucaus26th.dto.response.foodTruck.MenuResponseDto;
import com.example.lucaus26th.global.api.ApiResponse;
import com.example.lucaus26th.service.foodTruck.FoodTruckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/foodtruck")
@RequiredArgsConstructor
@Tag(name = "푸드트럭 관리", description = "푸드트럭 등록/수정/삭제 관련 API")
public class FoodTruckAdminController {

    private final FoodTruckService foodTruckService;

    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "푸드트럭 생성", description = "새로운 푸드트럭을 생성합니다.")
    public ResponseEntity<ApiResponse<FoodTruckResponseDto>> createFoodTruck(
            @ModelAttribute FoodTruckRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(foodTruckService.createFoodTruck(dto)));
    }

    @PatchMapping(value = "/{foodTruckId}", consumes = "multipart/form-data")
    @Operation(summary = "푸드트럭 수정", description = "foodTruckId에 해당하는 푸드트럭 정보를 수정합니다.")
    public ResponseEntity<ApiResponse<FoodTruckResponseDto>> updateFoodTruck(
            @PathVariable Long foodTruckId,
            @ModelAttribute FoodTruckRequestDto dto
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(foodTruckService.updateFoodTruck(foodTruckId, dto))
        );
    }

    @DeleteMapping("/{foodTruckId}")
    public ResponseEntity<ApiResponse<Void>> deleteFoodTruck(
            @PathVariable Long foodTruckId
    ) {
        foodTruckService.deleteFoodTruck(foodTruckId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 메뉴
    @PostMapping(value = "/{foodTruckId}/menu", consumes = "multipart/form-data")
    @Operation(summary = "푸드트럭 메뉴 생성", description = "foodTruckId에 해당하는 푸드트럭에 새로운 메뉴를 생성합니다.")
    public ResponseEntity<ApiResponse<MenuResponseDto>> createMenu(
            @PathVariable Long foodTruckId,
            @ModelAttribute MenuRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(foodTruckService.createMenu(foodTruckId, dto)));
    }

    @PatchMapping(value = "/{foodTruckId}/menu/{menuId}", consumes = "multipart/form-data")
    @Operation(summary = "푸드트럭 메뉴 수정", description = "foodTruckId, menuId에 해당하는 메뉴를 수정합니다.")
    public ResponseEntity<ApiResponse<MenuResponseDto>> updateMenu(
            @PathVariable Long foodTruckId,
            @PathVariable Long menuId,
            @ModelAttribute MenuRequestDto dto
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(foodTruckService.updateMenu(foodTruckId, menuId, dto))
        );
    }

    @DeleteMapping("/{foodTruckId}/menu/{menuId}")
    public ResponseEntity<ApiResponse<Void>> deleteMenu(
            @PathVariable Long foodTruckId,
            @PathVariable Long menuId
    ) {
        foodTruckService.deleteMenu(foodTruckId, menuId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}