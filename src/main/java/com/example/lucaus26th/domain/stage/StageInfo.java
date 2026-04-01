package com.example.lucaus26th.domain.stage;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stage_info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private StageInfo(
            String instagram,
            String youtube,
            String performerImage,
            String info
    ) {
        this.instagram = instagram;
        this.youtube = youtube;
        this.performerImage = performerImage;
        this.info = info;
    }

    public static StageInfo create(
            String instagram,
            String youtube,
            String performerImage,
            String info
    ) {
        return new StageInfo(instagram, youtube, performerImage, info);
    }

    public void assignStage(Stage stage) {
        this.stage = stage;
    }

    public void removeStage() {
        this.stage = null;
    }

    public void update(
            String instagram,
            String youtube,
            String performerImage,
            String info
    ) {
        if (instagram != null) this.instagram = instagram;
        if (youtube != null) this.youtube = youtube;
        if (performerImage != null) this.performerImage = performerImage;
        if (info != null) this.info = info;
    }

}
