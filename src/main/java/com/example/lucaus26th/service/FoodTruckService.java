package com.example.lucaus26th.service;

import com.example.lucaus26th.domain.Setting;
import com.example.lucaus26th.domain.food.FoodTruck;
import com.example.lucaus26th.dto.request.FoodTruckRequestDto;
import com.example.lucaus26th.repository.FoodTruckRepository;
import com.example.lucaus26th.repository.SettingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class FoodTruckService {
    private final FoodTruckRepository foodTruckRepository;
    private final SettingRepository settingRepository;

    public Long createFoodTruck(FoodTruckRequestDto request) {
        Setting setting = null;

        if (request.getSettingId() != null) {
            setting = settingRepository.findById(request.getSettingId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 setting입니다."));
        }
        FoodTruck foodTruck = new FoodTruck(
            setting,
            request.getName(),
            request.getLocationId(),
            request.getLocation(),
            request.getImage(),
            request.getBestMenu(),
            0L
        );

        FoodTruck savedFoodTruck = foodTruckRepository.save(foodTruck);
        return savedFoodTruck.getId();
        }
}
