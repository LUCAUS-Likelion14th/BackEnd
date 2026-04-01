package com.example.lucaus26th.repository.food;

import com.example.lucaus26th.domain.food.FoodLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodLikeRepository extends JpaRepository<FoodLike, Long> {

    boolean existsByFoodTruckIdAndMemberId(Long foodTruckId, Long memberId);
    void deleteByFoodTruckIdAndMemberId(Long memberId, Long foodTruckId);
}