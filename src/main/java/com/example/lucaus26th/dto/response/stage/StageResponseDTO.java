package com.example.lucaus26th.dto.response.stage;

import com.example.lucaus26th.domain.stage.Stage;
import com.example.lucaus26th.enums.StageCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "stage_id", "start_at", "end_at", "time", "status", "logo_image", "performer", "category" })
public class StageResponseDTO {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final ZoneId KOREA_ZONE = ZoneId.of("Asia/Seoul");

    @JsonProperty("stage_id")
    private Long stageId;

    @JsonProperty("start_at")
    private LocalDateTime startAt;

    @JsonProperty("end_at")
    private LocalDateTime endAt;

    private String time;
    private String status;

    @JsonProperty("logo_image")
    private String logoImage;

    private String performer;
    private String category;

    public static StageResponseDTO from(Stage stage){
        LocalDateTime startDateTime = LocalDateTime.of(stage.getDate(), stage.getStartAt());
        LocalDateTime endDateTime = LocalDateTime.of(stage.getDate(), stage.getEndAt());

        String formattedStartAt = stage.getStartAt().format(TIME_FORMATTER);
        String formattedEndAt = stage.getEndAt().format(TIME_FORMATTER);

        return StageResponseDTO.builder()
                .stageId(stage.getId())
                .startAt(startDateTime)
                .endAt(endDateTime)
                .time(formattedStartAt + " - " + formattedEndAt)
                .status(calculateStatus(startDateTime, endDateTime))
                .logoImage(stage.getLogoImage())
                .performer(stage.getPerformer())
                .category(stage.getCategory().getDisplayName())
                .build();
    }

    // 연속된 아티스트 공연들을 하나의 묶음 응답으로 변환
    public static StageResponseDTO fromArtistGroup(List<Stage> group, String groupLogoImage){
        Stage first = group.get(0);
        Stage last = group.get(group.size() - 1);

        LocalDateTime startDateTime = LocalDateTime.of(first.getDate(), first.getStartAt());
        LocalDateTime endDateTime = LocalDateTime.of(last.getDate(), last.getEndAt());

        String formattedStartAt = first.getStartAt().format(TIME_FORMATTER);
        String formattedEndAt = last.getEndAt().format(TIME_FORMATTER);

        return StageResponseDTO.builder()
                .stageId(null)
                .startAt(startDateTime)
                .endAt(endDateTime)
                .time(formattedStartAt + " - " + formattedEndAt)
                .status(calculateStatus(startDateTime, endDateTime))
                .logoImage(groupLogoImage)
                .performer(StageCategory.ARTIST_PERFORMANCE.getDisplayName())
                .category(StageCategory.ARTIST_PERFORMANCE.getDisplayName())
                .build();
    }

    // endAt은 배타적(exclusive)으로 처리: 정확히 endAt 시점은 이미 끝난 것으로 본다.
    // (예: A=18:00-19:30, B=19:30-20:00인 경우 19:30에는 A=PAST, B=CURRENT)
    private static String calculateStatus(LocalDateTime startAt, LocalDateTime endAt){
        LocalDateTime now = LocalDateTime.now(KOREA_ZONE);

        if(!now.isBefore(endAt)){
            return "PAST";
        }

        if(!now.isBefore(startAt)){
            return "CURRENT";
        }

        return "UPCOMING";
    }
}
