package com.example.lucaus26th.repository.foodTruck;

import com.example.lucaus26th.domain.foodTruck.FoodTruck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodTruckRepository extends JpaRepository<FoodTruck, Long> {
    List<FoodTruck> findTop3ByOrderByLikeCountDesc();
}
