package com.example.lucaus26th.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stage")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private List<Song> songs = new ArrayList<>();

    @OneToOne(mappedBy = "stage", cascade = CascadeType.ALL, orphanRemoval = true)
    private StageInfo stageInfo;

    // 공연 정보 설정
    private Stage(
            StageCategory category,
            LocalTime startAt,
            LocalTime endAt,
            LocalDate date,
            String performer,
            String logo
    ) {
        this.category = category;
        this.startAt = startAt;
        this.endAt = endAt;
        this.date = date;
        this.performer = performer;
        this.logo = logo;
    }

    //필수정보 검증 후 공연 생성
    public static Stage create(
            StageCategory category,
            LocalTime startAt,
            LocalTime endAt,
            LocalDate date,
            String performer,
            String logo
    ) {
        validateRequiredFields(category, startAt, endAt, date, performer);
        validateTime(startAt, endAt);

        return new Stage(category, startAt, endAt, date, performer, logo);
    }

    public void updateStage(
            StageCategory category,
            LocalTime startAt,
            LocalTime endAt,
            LocalDate date,
            String performer,
            String logo
    ) {
        if (category != null) this.category = category;
        if (date != null) this.date = date;
        if (performer != null && !performer.isBlank()) this.performer = performer;
        if (logo != null) this.logo = logo;

        LocalTime newStartAt = (startAt != null) ? startAt : this.startAt;
        LocalTime newEndAt = (endAt != null) ? endAt : this.endAt;
        validateTime(newStartAt, newEndAt);

        if (startAt != null) {
            this.startAt = startAt;
        }
        if (endAt != null) {
            this.endAt = endAt;
        }
    }

    public void connectStageInfo(StageInfo stageInfo) {
        this.stageInfo = stageInfo;
    }

    public void disconnectStageInfo() {
        this.stageInfo = null;
    }

    //필수정보 다 포함되는지 검사
    private static void validateRequiredFields(
            StageCategory category,
            LocalTime startAt,
            LocalTime endAt,
            LocalDate date,
            String performer
    ) {
        if (category == null || startAt == null || endAt == null || date == null ||
                performer == null || performer.isBlank()) {
            throw new IllegalArgumentException("공연 기본 정보(category, startAt, endAt, date, performer)는 필수입니다.");
        }
    }

    //시간 검증
    private static void validateTime(LocalTime startAt, LocalTime endAt) {
        if (startAt != null && endAt != null && !startAt.isBefore(endAt)) {
            throw new IllegalArgumentException("공연 시작 시간은 종료 시간보다 빨라야 합니다.");
        }
    }

}
