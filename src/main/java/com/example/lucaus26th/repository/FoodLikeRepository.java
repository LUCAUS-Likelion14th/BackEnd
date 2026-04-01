package com.example.lucaus26th.repository;

import com.example.lucaus26th.domain.food.FoodLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FoodLikeRepository extends JpaRepository<FoodLike, Long> {
    @Query("select fl.foodTruck.id from FoodLike fl where fl.member.id = :memberId")
    List<Long> findFoodTruckIdsByMemberId(Long memberId);
}
