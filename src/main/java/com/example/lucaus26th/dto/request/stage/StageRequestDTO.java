package com.example.lucaus26th.dto.request.stage;

import com.example.lucaus26th.enums.StageCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
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

    @Schema(type = "string", format = "binary", description = "로고 이미지 파일")
    private MultipartFile logoImage;

    private String instagram;
    private String youtube;

    @Schema(type = "string", format = "binary", description = "공연자 이미지 파일")
    private MultipartFile performerImage;

    private String info;

    //TODO: stageinfo 따로 입력받는 버전으로 수정 고려
    public boolean hasStageInfoField(){
        return (instagram != null && !instagram.isBlank())
                || (youtube != null && !youtube.isBlank())
                || (performerImage != null && !performerImage.isEmpty())
                || (info != null && !info.isBlank());
    }
}
