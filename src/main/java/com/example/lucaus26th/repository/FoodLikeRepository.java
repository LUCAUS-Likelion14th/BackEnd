package com.example.lucaus26th.repository;

import com.example.lucaus26th.domain.food.FoodLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodLikeRepository extends JpaRepository<FoodLike, Long> {

    boolean existsByFoodTruckIdAndMemberId(Long foodTruckId, Long memberId);

    @Query("select fl.foodTruck.id from FoodLike fl where fl.member.id = :memberId")
    List<Long> findFoodTruckIdsByMemberId(@Param("memberId") Long memberId);
}