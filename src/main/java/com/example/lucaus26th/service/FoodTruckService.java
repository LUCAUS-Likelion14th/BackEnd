package com.example.lucaus26th.service;

import com.example.lucaus26th.domain.Setting;
import com.example.lucaus26th.domain.food.FoodTruck;
import com.example.lucaus26th.dto.request.FoodTruckRequestDto;
import com.example.lucaus26th.dto.response.FoodTruckResponseDto;
import com.example.lucaus26th.repository.FoodLikeRepository;
import com.example.lucaus26th.repository.FoodTruckRepository;
import com.example.lucaus26th.repository.SettingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class FoodTruckService {
    private final FoodTruckRepository foodTruckRepository;
    private final FoodLikeRepository foodLikeRepository;
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

    public Long updateFoodTruck(Long foodTruckId, FoodTruckRequestDto request) {
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 푸드트럭입니다."));

        Setting setting = foodTruck.getSetting();
        if (request.getSettingId() != null) {
            setting = settingRepository.findById(request.getSettingId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 setting입니다."));
        }

        foodTruck.update(
                setting,
                request.getName(),
                request.getLocationId(),
                request.getLocation(),
                request.getImage(),
                request.getBestMenu()
        );

        return foodTruck.getId();
    }

    public void deleteFoodTruck(Long foodTruckId) {
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 푸드트럭입니다."));

        foodTruckRepository.delete(foodTruck);
    }

    public List<FoodTruckResponseDto> getAllFoodTrucks(Long memberId) {
        List<FoodTruck> foodTrucks = foodTruckRepository.findAll();

        Set<Long> likedSet;

        if (memberId != null) {
            likedSet = new HashSet<>(
                    foodLikeRepository.findFoodTruckIdsByMemberId(memberId)
            );
        } else {
            likedSet = new HashSet<>();
        }

        return foodTrucks.stream()
                .map(foodTruck -> {
                    boolean isLiked = likedSet.contains(foodTruck.getId());
                    return FoodTruckResponseDto.from(foodTruck, isLiked);
                })
                .toList();
    }

}
