package com.example.lucaus26th.domain.food;


import com.example.lucaus26th.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class FoodTruckLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "foodtruck_id", nullable = false)
    private FoodTruck foodTruck;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public FoodTruckLike(FoodTruck foodTruck, Member member) {
        this.foodTruck = foodTruck;
        this.member = member;
    }
}
