package com.example.lucaus26th.service;

import com.example.lucaus26th.domain.Setting;
import com.example.lucaus26th.domain.food.FoodTruck;
import com.example.lucaus26th.dto.request.FoodTruckRequestDto;
import com.example.lucaus26th.dto.response.FoodTruckResponseDto;
import com.example.lucaus26th.repository.FoodLikeRepository;
import com.example.lucaus26th.repository.FoodTruckRepository;
import com.example.lucaus26th.repository.booth.SettingRepository;
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

        if (request.getSetting() != null) {
            FoodTruckRequestDto.SettingRequest sr = request.getSetting();

            setting = Setting.builder()
                    .mon(sr.getMon())
                    .tue(sr.getTue())
                    .wed(sr.getWed())
                    .thu(sr.getThu())
                    .fri(sr.getFri())
                    .build();
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

        foodTruck.update(
                request.getName(),
                request.getLocationId(),
                request.getLocation(),
                request.getImage(),
                request.getBestMenu()
        );

        if (request.getSetting() != null) {
            FoodTruckRequestDto.SettingRequest sr = request.getSetting();

            if (foodTruck.getSetting() == null) {
                Setting newSetting = Setting.builder()
                        .mon(sr.getMon())
                        .tue(sr.getTue())
                        .wed(sr.getWed())
                        .thu(sr.getThu())
                        .fri(sr.getFri())
                        .build();

                foodTruck.setSetting(newSetting);
            } else {
                Setting setting = foodTruck.getSetting();
                setting.setMon(sr.getMon());
                setting.setTue(sr.getTue());
                setting.setWed(sr.getWed());
                setting.setThu(sr.getThu());
                setting.setFri(sr.getFri());
            }
        }

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

    public FoodTruckResponseDto getFoodTruck(Long foodTruckId, Long memberId) {
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 푸드트럭입니다."));

        boolean isLiked = false;
        if (memberId != null) {
            isLiked = foodLikeRepository.existsByFoodTruckIdAndMemberId(foodTruckId, memberId);
        }

        return FoodTruckResponseDto.from(foodTruck, isLiked);
    }

}
