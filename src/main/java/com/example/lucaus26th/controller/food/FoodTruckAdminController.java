package com.example.lucaus26th.controller.food;

import com.example.lucaus26th.dto.request.food.FoodTruckRequestDto;
import com.example.lucaus26th.dto.request.food.MenuRequestDto;
import com.example.lucaus26th.dto.response.food.FoodTruckResponseDto;
import com.example.lucaus26th.dto.response.food.MenuResponseDto;
import com.example.lucaus26th.service.food.FoodTruckService;
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

    // 푸드트럭 메뉴 관련 api
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
}
