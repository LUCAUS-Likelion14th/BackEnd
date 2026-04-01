package com.example.lucaus26th.controller;

import com.example.lucaus26th.domain.food.FoodTruck;
import com.example.lucaus26th.dto.request.FoodTruckRequestDto;
import com.example.lucaus26th.dto.response.FoodTruckResponseDto;
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
    public ResponseEntity<String> createFoodTruck(@RequestBody FoodTruckRequestDto dto){
        Long id = foodTruckService.createFoodTruck(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("푸드트럭 생성 성공: " + id);
    }

    @PatchMapping("/{foodTruckId}")
    public ResponseEntity<String> updateFoodTruck(@PathVariable Long foodTruckId,
                                                  @RequestBody FoodTruckRequestDto dto){
        Long id = foodTruckService.updateFoodTruck(foodTruckId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("푸드트럭 수정 성공: " + id);
    }

    @DeleteMapping("/{foodTruckId}")
    public ResponseEntity<String> deleteFoodTruck(@PathVariable Long foodTruckId) {
        foodTruckService.deleteFoodTruck(foodTruckId);
        return ResponseEntity.ok("푸드트럭 삭제 성공: " + foodTruckId);
    }

    @GetMapping
    public ResponseEntity<List<FoodTruckResponseDto>> getAllFoodTrucks(Authentication authentication) {

        Long memberId = null;

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof Long) {
            memberId = (Long) authentication.getPrincipal();
        }

        return ResponseEntity.ok(foodTruckService.getAllFoodTrucks(memberId));
    }


}
