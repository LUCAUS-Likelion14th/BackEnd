package com.example.lucaus26th.service;

import com.example.lucaus26th.domain.Member;
import com.example.lucaus26th.domain.Setting;
import com.example.lucaus26th.domain.food.FoodLike;
import com.example.lucaus26th.domain.food.FoodTruck;
import com.example.lucaus26th.dto.request.FoodTruckRequestDto;
import com.example.lucaus26th.dto.response.FoodTruckResponseDto;
import com.example.lucaus26th.repository.food.FoodLikeRepository;
import com.example.lucaus26th.repository.food.FoodTruckRepository;
import com.example.lucaus26th.repository.booth.SettingRepository;
import com.example.lucaus26th.repository.food.MemberRepository;
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
    private final MemberRepository memberRepository;

    public Long createFoodTruck(FoodTruckRequestDto request) {
        FoodTruck foodTruck = new FoodTruck(
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

        return foodTruck.getId();
    }

    public void deleteFoodTruck(Long foodTruckId) {
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 푸드트럭입니다."));

        foodTruckRepository.delete(foodTruck);
    }

    public void createFoodLike(Long foodTruckId, Long memberId) {

        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 푸드트럭입니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (foodLikeRepository.existsByFoodTruckIdAndMemberId(foodTruckId, memberId)) {
            throw new IllegalStateException("이미 좋아요를 누른 푸드트럭입니다.");
        }

        FoodLike foodLike = new FoodLike(foodTruck, member);
        foodLikeRepository.save(foodLike);

        foodTruck.increaseLikeCount();
    }

    @Transactional
    public void deleteFoodLike(Long foodTruckId, Long memberId) {
        foodLikeRepository.deleteByFoodTruckIdAndMemberId(memberId, foodTruckId);
    }


}
