package com.example.lucaus26th.repository.foodTruck;

import com.example.lucaus26th.domain.foodTruck.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByIdAndFoodTruckId(Long menuId, Long foodTruckId);
}
