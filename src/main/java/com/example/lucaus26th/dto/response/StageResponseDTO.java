package com.example.lucaus26th.dto.response;

import com.example.lucaus26th.domain.Stage;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@JsonPropertyOrder({ "stage_id", "start_at", "end_at", "time", "status", "performer_logo", "performer", "category" })
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

    @JsonProperty("performer_logo")
    private String performerLogo;

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
                .performerLogo(stage.getLogo())
                .performer(stage.getPerformer())
                .category(stage.getCategory().getDisplayName())
                .build();
    }

    private static String calculateStatus(LocalDateTime startAt, LocalDateTime endAt){
        LocalDateTime now = LocalDateTime.now(KOREA_ZONE);

        if(now.isAfter(endAt)){
            return "PAST";
        }

        if(!now.isBefore(startAt) && now.isBefore(endAt)){
            return "CURRENT";
        }

        return "UPCOMING";
    }
}
