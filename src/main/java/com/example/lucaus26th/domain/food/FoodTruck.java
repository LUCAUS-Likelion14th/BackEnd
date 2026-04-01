package com.example.lucaus26th.domain.food;

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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long locationId;

    @Column(nullable = false)
    private String location;

    private String image;
    private String bestMenu;
    private Long likeCount;

    @OneToMany(mappedBy = "foodTruck", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodTruckSetting> settings = new ArrayList<>();


    @OneToMany(mappedBy = "foodTruck", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "foodTruck", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodLike> foodLikes = new ArrayList<>();


    public FoodTruck(String name, Long locationId, String location,
                     String image, String bestMenu, Long likeCount) {
        this.name = name;
        this.locationId = locationId;
        this.location = location;
        this.image = image;
        this.bestMenu = bestMenu;
        this.likeCount = likeCount;
    }

    public void update(String name, Long locationId, String location,
                       String image, String bestMenu) {
        this.name = name;
        this.locationId = locationId;
        this.location = location;
        this.image = image;
        this.bestMenu = bestMenu;
    }

    public void addSetting(FoodTruckSetting setting) {
        settings.add(setting);
        setting.setFoodTruck(this);
    }

    public void increaseLikeCount() {
        if (this.likeCount == null) this.likeCount = 0L;
        this.likeCount++;
    }
}