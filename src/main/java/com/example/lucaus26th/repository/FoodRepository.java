package com.example.lucaus26th.repository;

import com.example.lucaus26th.domain.food.FoodTruck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<FoodTruck, Long> {
}
