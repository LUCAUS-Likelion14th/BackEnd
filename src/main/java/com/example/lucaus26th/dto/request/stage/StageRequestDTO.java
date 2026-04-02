package com.example.lucaus26th.dto.request.stage;

import com.example.lucaus26th.enums.StageCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class StageRequestDTO {

    private StageCategory category;

    @Schema(description = "공연 시작 시간", example = "18:00")
    private LocalTime startAt;

    @Schema(description = "공연 종료 시간", example = "19:30")
    private LocalTime endAt;

    private String performer;

    @Schema(description = "공연 날짜", example = "2026-04-01")
    private LocalDate date;

    private String logo;
    private String instagram;
    private String youtube;
    private String performerImage;
    private String info;

    //TODO: stageinfo 따로 입력받는 버전으로 수정 고려
    public boolean hasStageInfoField(){
        return instagram != null || youtube != null || performerImage != null || info != null;
    }
}
