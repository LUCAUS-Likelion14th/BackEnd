package com.example.lucaus26th.domain.food;


import com.example.lucaus26th.domain.Setting;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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


}
