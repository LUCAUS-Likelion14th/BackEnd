package com.example.lucaus26th.domain.foodTruck;

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
    private String foodTruckInfo;

    @OneToMany(mappedBy = "foodTruck", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodTruckSetting> settings = new ArrayList<>();


    @OneToMany(mappedBy = "foodTruck", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "foodTruck", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodTruckLike> foodTruckLikes = new ArrayList<>();


    public FoodTruck(String name, Long locationId, String location,
                     String image, String bestMenu, Long likeCount, String foodTruckInfo) {
        this.name = name;
        this.locationId = locationId;
        this.location = location;
        this.image = image;
        this.bestMenu = bestMenu;
        this.likeCount = likeCount;
        this.foodTruckInfo = foodTruckInfo;
    }

    public void update(String name, Long locationId, String location,
                       String image, String bestMenu, String foodTruckInfo) {
        this.name = name;
        this.locationId = locationId;
        this.location = location;
        this.image = image;
        this.bestMenu = bestMenu;
        this.foodTruckInfo = foodTruckInfo;
    }

    public void addSetting(FoodTruckSetting setting) {
        settings.add(setting);
    }

    public void increaseLikeCount() {
        if (this.likeCount == null) this.likeCount = 0L;
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
}