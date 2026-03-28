package com.example.lucaus26th.domain.booth;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoothCategory {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;


    @ManyToOne
    @JoinColumn(name = "booth_id")
    private Booth booth;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
