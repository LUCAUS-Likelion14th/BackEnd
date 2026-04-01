package com.example.lucaus26th.domain.booth;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoothCategory {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "booth_id")
    private Booth booth;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder  // ← 추가
    public BoothCategory(Booth booth, Category category) {
        this.booth = booth;
        this.category = category;
    }

}
