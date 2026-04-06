package com.example.lucaus26th.domain.foodTruck;

import jakarta.persistence.*;
import lombok.Builder;
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
    @JoinColumn(name = "food_truck_id", nullable = false)
    private FoodTruck foodTruck;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    private String image;

    @Builder
    public Menu(String name, Long price, String image, FoodTruck foodTruck) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.foodTruck = foodTruck;
    }

    public void update(String name, Long price, String image) {
        if (name != null) this.name = name;
        if (price != null) this.price = price;
        if (image != null) this.image = image;
    }
}
