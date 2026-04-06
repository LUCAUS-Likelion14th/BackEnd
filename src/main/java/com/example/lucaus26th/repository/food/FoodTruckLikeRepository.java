package com.example.lucaus26th.repository.food;

import com.example.lucaus26th.domain.Member;
import com.example.lucaus26th.domain.food.FoodTruckLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodTruckLikeRepository extends JpaRepository<FoodTruckLike, Long> {

    boolean existsByFoodTruckIdAndMemberId(Long foodTruckId, Long memberId);
    void deleteByFoodTruckIdAndMemberId(Long foodTruckId, Long memberId);
    List<FoodTruckLike> findByMember(Member member);
}