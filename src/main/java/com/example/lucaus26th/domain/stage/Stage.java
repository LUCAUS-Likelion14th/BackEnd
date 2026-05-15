package com.example.lucaus26th.domain.stage;

import com.example.lucaus26th.enums.StageCategory;
import com.example.lucaus26th.enums.StageVisibility;
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

    @Column(name = "logo_image")
    private String logoImage;

    // 공연 가시성 범위 — 어떤 엔드포인트에 노출할지 결정
    // DEFAULT: 전체 노출 / TIMETABLE_ONLY: 타임테이블·라이브만 / LINEUP_ONLY: 라인업·상세만
    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false)
    private StageVisibility visibility;

    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("playOrder ASC")
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
            String logoImage,
            StageVisibility visibility
    ) {
        this.category = category;
        this.startAt = startAt;
        this.endAt = endAt;
        this.date = date;
        this.performer = performer;
        this.logoImage = logoImage;
        this.visibility = visibility;
    }

    //필수정보 검증 후 공연 생성
    public static Stage create(
            StageCategory category,
            LocalTime startAt,
            LocalTime endAt,
            LocalDate date,
            String performer,
            String logoImage,
            StageVisibility visibility
    ) {
        validateRequiredFields(category, startAt, endAt, date, performer);
        validateTime(startAt, endAt);

        StageVisibility resolvedVisibility = (visibility != null) ? visibility : StageVisibility.DEFAULT;
        return new Stage(category, startAt, endAt, date, performer, logoImage, resolvedVisibility);
    }

    public void updateStage(
            StageCategory category,
            LocalTime startAt,
            LocalTime endAt,
            LocalDate date,
            String performer,
            String logoImage,
            StageVisibility visibility
    ) {
        if (category != null) this.category = category;
        if (date != null) this.date = date;
        if (performer != null && !performer.isBlank()) this.performer = performer;
        if (logoImage != null) this.logoImage = logoImage;
        if (visibility != null) this.visibility = visibility;

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
        if(stageInfo != null) {
            stageInfo.assignStage(this);
        }
    }

    public void disconnectStageInfo() {
        if(this.stageInfo != null){
            this.stageInfo.removeStage();
            this.stageInfo = null;
        }
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

    //곡 추가
    public void addSong(Song song) {
        this.songs.add(song);
        song.assignStage(this);
    }

    //곡 삭제
    public void removeSong(Song song) {
        this.songs.remove(song);
    }

}
