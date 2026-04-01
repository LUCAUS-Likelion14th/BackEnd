package com.example.lucaus26th.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stage_info")
@Getter
@NoArgsConstructor
public class StageInfo {

    @Id
    private Long id;

    @Column
    private String instagram;

    @Column
    private String youtube;

    @Column(name = "performer_image")
    private String performerImage;

    @Lob
    private String info;

    @MapsId
    @OneToOne
    @JoinColumn(name = "stage_id")
    private Stage stage;

}
