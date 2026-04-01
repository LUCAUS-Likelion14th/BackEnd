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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "setting_id")
    private Setting setting;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long locationId;

    @Column(nullable = false)
    private String location;

    private String image;
    private String bestMenu;
    private Long likeCount;

    public FoodTruck(Setting setting, String name, Long locationId, String location,
                     String image, String bestMenu, Long likeCount) {
        this.setting = setting;
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

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    @OneToMany(mappedBy = "foodTruck", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "foodTruck", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodLike> foodLikes = new ArrayList<>();
}