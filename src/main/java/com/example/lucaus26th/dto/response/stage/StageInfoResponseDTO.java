package com.example.lucaus26th.dto.response.stage;

import com.example.lucaus26th.domain.stage.Stage;
import com.example.lucaus26th.domain.stage.StageInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
@JsonPropertyOrder({ "stage_id", "time", "stage_info", "performer", "performer_image", "instagram", "youtube", "songs" })
public class StageInfoResponseDTO {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @JsonProperty("stage_id")
    private Long stageId;

    private String time;

    @JsonProperty("stage_info")
    private String stageInfo;

    private String performer;

    @JsonProperty("performer_image")
    private String performerImage;

    private String instagram;
    private String youtube;

    private List<SongResponseDTO> songs;

    public static StageInfoResponseDTO from(Stage stage) {
        StageInfo info = stage.getStageInfo();

        String formattedStartAt = stage.getStartAt().format(TIME_FORMATTER);
        String formattedEndAt = stage.getEndAt().format(TIME_FORMATTER);

        return StageInfoResponseDTO.builder()
                .stageId(stage.getId())
                .time(formattedStartAt + " - " + formattedEndAt)
                .stageInfo(info.getInfo())
                .performer(stage.getPerformer())
                .performerImage(info.getPerformerImage())
                .instagram(info.getInstagram())
                .youtube(info.getYoutube())
                .songs(stage.getSongs().stream()
                        .map(SongResponseDTO::from)
                        .toList())
                .build();
    }
}
