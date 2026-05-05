package com.example.lucaus26th.repository.foodTruck;

import com.example.lucaus26th.domain.foodTruck.FoodTruckSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FoodTruckSettingRepository extends JpaRepository<FoodTruckSetting, Long> {

    List<FoodTruckSetting> findAllByFoodTruckId(Long foodTruckId);

    Optional<FoodTruckSetting> findByIdAndFoodTruckId(Long id, Long foodTruckId);
}