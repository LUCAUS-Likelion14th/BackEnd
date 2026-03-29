package com.example.lucaus26th.repository;

import com.example.lucaus26th.domain.food.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
