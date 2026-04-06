package com.example.lucaus26th.domain;

import com.example.lucaus26th.dto.request.PromotionRequestDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "promotion")
@Getter
@NoArgsConstructor
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String image;

    private String instagram;

    private Promotion(String image, String instagram) {
        this.image = image;
        this.instagram = instagram;
    }

    public static Promotion create(String image, String instagram) {
        return new Promotion(image, instagram);
    }

    public void updatePromotion(String image, String instagram) {
        this.image = image;
        this.instagram = instagram;
    }
}
