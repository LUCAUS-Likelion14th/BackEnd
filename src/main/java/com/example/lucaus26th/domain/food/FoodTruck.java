package com.example.lucaus26th.domain.food;


import com.example.lucaus26th.domain.Setting;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class FoodTruck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "setting_id")
    private Setting setting;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long location_id;

    @Column(nullable = false)
    private String location;

    private String image;
    private String best_menu;
    private Long like_count;

    public FoodTruck(Setting setting, String name, Long location_id, String location,
                     String image, String best_menu, Long like_count) {
        this.setting = setting;
        this.name = name;
        this.location_id = location_id;
        this.location = location;
        this.image = image;
        this.best_menu = best_menu;
        this.like_count = like_count;
    }

    @OneToMany(mappedBy = "foodTruck", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "foodTruck", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodLike> foodLikes = new ArrayList<>();

}
