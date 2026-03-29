package com.example.lucaus26th.domain.food;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "foodtruck_id")
    private FoodTruck foodTruck;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    private String image;
}
