package com.example.lucaus26th.controller;

import com.example.lucaus26th.dto.request.FoodTruckRequestDto;
import com.example.lucaus26th.service.FoodTruckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/foodtruck")
@RequiredArgsConstructor
public class FoodTruckController {
    private final FoodTruckService foodTruckService;

    @PostMapping
    public ResponseEntity<String> createFoodTruck(@RequestBody FoodTruckRequestDto dto){
        Long foodTruckId = foodTruckService.createFoodTruck(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("푸드트럭 생성 성공: " + foodTruckId);
    }
}
