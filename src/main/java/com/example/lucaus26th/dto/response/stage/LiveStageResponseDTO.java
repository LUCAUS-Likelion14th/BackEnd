package com.example.lucaus26th.dto.response.stage;

import com.example.lucaus26th.domain.stage.Stage;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"stage_id", "performer", "logo", "time"})
public class LiveStageResponseDTO {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @JsonProperty("stage_id")
    private Long stageId;

    private String performer;
    private String logoImage;
    private String time;

    public static LiveStageResponseDTO from(Stage stage){

        String formattedStartAt = stage.getStartAt().format(TIME_FORMATTER);
        String formattedEndAt = stage.getEndAt().format(TIME_FORMATTER);

        return LiveStageResponseDTO.builder()
                .stageId(stage.getId())
                .performer(stage.getPerformer())
                .logoImage(stage.getLogoImage())
                .time(formattedStartAt + " - " + formattedEndAt)
                .build();
    }
}
