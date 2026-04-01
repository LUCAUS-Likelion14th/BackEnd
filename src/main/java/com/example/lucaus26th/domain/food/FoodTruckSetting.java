package com.example.lucaus26th.domain.food;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
public class FoodTruckSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foodtruck_id", nullable = false)
    private FoodTruck foodTruck;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "day_of_week", nullable = false)
    private String day;

    @Column(nullable = false)
    private LocalTime startAt;

    @Column(nullable = false)
    private LocalTime endAt;

    public FoodTruckSetting(FoodTruck foodTruck, LocalDate date, String day, LocalTime startAt, LocalTime endAt) {
        this.foodTruck = foodTruck;
        this.date = date;
        this.day = day;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public void setFoodTruck(FoodTruck foodTruck) {
        this.foodTruck = foodTruck;
    }
}