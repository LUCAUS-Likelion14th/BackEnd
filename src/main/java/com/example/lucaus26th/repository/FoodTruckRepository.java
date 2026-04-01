package com.example.lucaus26th.repository;

import com.example.lucaus26th.domain.food.FoodTruck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FoodTruckRepository extends JpaRepository<FoodTruck, Long> {

}
