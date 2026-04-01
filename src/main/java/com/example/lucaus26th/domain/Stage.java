package com.example.lucaus26th.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "stage")
@Getter
@NoArgsConstructor
public class Stage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StageCategory category;

    @Column(name = "start_at", nullable = false)
    private LocalTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalTime endAt;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, length = 100)
    private String performer;

    @Column(name = "logo")
    private String logo;

    @OneToMany(mappedBy = "stage")
    private List<Song> songs;

    @OneToOne(mappedBy = "stage", cascade = CascadeType.ALL)
    private StageInfo stageInfo;

}
