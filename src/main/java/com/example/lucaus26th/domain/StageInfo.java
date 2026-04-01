package com.example.lucaus26th.domain;

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

    public StageInfo(
            Stage stage,
            String instagram,
            String youtube,
            String performerImage,
            String info
    ) {
        this.stage = stage;
        this.instagram = instagram;
        this.youtube = youtube;
        this.performerImage = performerImage;
        this.info = info;
    }

    public static StageInfo create(
            Stage stage,
            String instagram,
            String youtube,
            String performerImage,
            String info
    ) {
        if (stage == null) {
            throw new IllegalArgumentException("공연 상세 정보는 공연 없이 생성할 수 없습니다.");
        }
        return new StageInfo(stage, instagram, youtube, performerImage, info);
    }

    public void update(
            String instagram,
            String youtube,
            String performerImage,
            String info
    ) {
        if (instagram != null) {this.instagram = instagram;
        }
        if (youtube != null) {this.youtube = youtube;
        }
        if (performerImage != null) {this.performerImage = performerImage;
        }
        if (info != null) {this.info = info;
        }
    }

}
