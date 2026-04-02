package com.example.lucaus26th.controller;

import com.example.lucaus26th.domain.food.FoodTruck;
import com.example.lucaus26th.dto.request.FoodTruckRequestDto;
import com.example.lucaus26th.dto.request.FoodTruckSettingRequestDto;
import com.example.lucaus26th.dto.request.MenuRequestDto;
import com.example.lucaus26th.dto.response.FoodTruckDetailResponseDto;
import com.example.lucaus26th.dto.response.FoodTruckResponseDto;
import com.example.lucaus26th.dto.response.FoodTruckSettingResponseDto;
import com.example.lucaus26th.dto.response.MenuResponseDto;
import com.example.lucaus26th.security.CustomUserDetails;
import com.example.lucaus26th.service.FoodTruckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/foodtruck")
@RequiredArgsConstructor
public class FoodTruckController {
    private final FoodTruckService foodTruckService;

    @PostMapping
    public ResponseEntity<FoodTruckResponseDto> createFoodTruck(@RequestBody FoodTruckRequestDto dto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(foodTruckService.createFoodTruck(dto));
    }

    @PatchMapping("/{foodTruckId}")
    public ResponseEntity<FoodTruckResponseDto> updateFoodTruck(
            @PathVariable Long foodTruckId,
            @RequestBody FoodTruckRequestDto dto){
        return ResponseEntity.ok(foodTruckService.updateFoodTruck(foodTruckId, dto));
    }

    @DeleteMapping("/{foodTruckId}")
    public ResponseEntity<Void> deleteFoodTruck(@PathVariable Long foodTruckId) {
        foodTruckService.deleteFoodTruck(foodTruckId);
        return ResponseEntity.noContent().build();
    }

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

    @PostMapping("/{foodTruckId}/menu")
    public ResponseEntity<MenuResponseDto> createMenu(
            @PathVariable Long foodTruckId,
            @RequestBody MenuRequestDto dto
    ) {
        return ResponseEntity.ok(foodTruckService.createMenu(foodTruckId, dto));
    }

    @PatchMapping("/{foodTruckId}/menu/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(
            @PathVariable Long foodTruckId,
            @PathVariable Long menuId,
            @RequestBody MenuRequestDto dto
    ) {
        return ResponseEntity.ok(foodTruckService.updateMenu(foodTruckId, menuId, dto));
    }

    @DeleteMapping("/{foodTruckId}/menu/{menuId}")
    public ResponseEntity<Void> deleteMenu(
            @PathVariable Long foodTruckId,
            @PathVariable Long menuId
    ) {
        foodTruckService.deleteMenu(foodTruckId, menuId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{foodTruckId}/setting")
    public ResponseEntity<FoodTruckSettingResponseDto> createSetting(
            @PathVariable Long foodTruckId,
            @RequestBody FoodTruckSettingRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(foodTruckService.createSetting(foodTruckId, dto));
    }

    @PatchMapping("/{foodTruckId}/setting/{settingId}")
    public ResponseEntity<FoodTruckSettingResponseDto> updateSetting(
            @PathVariable Long foodTruckId,
            @PathVariable Long settingId,
            @RequestBody FoodTruckSettingRequestDto dto
    ) {
        return ResponseEntity.ok(foodTruckService.updateSetting(foodTruckId, settingId, dto));
    }

    @DeleteMapping("/{foodTruckId}/setting/{settingId}")
    public ResponseEntity<Void> deleteSetting(
            @PathVariable Long foodTruckId,
            @PathVariable Long settingId
    ) {
        foodTruckService.deleteSetting(foodTruckId, settingId);
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
