package com.example.lucaus26th.dto.response.stage;

import com.example.lucaus26th.domain.stage.Stage;
import com.example.lucaus26th.domain.stage.StageInfo;
import com.example.lucaus26th.enums.StageCategory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "stage_id", "date", "time", "stage_info", "performer", "performer_image", "instagram", "youtube", "songs" })
public class StageInfoResponseDTO {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @JsonProperty("stage_id")
    private Long stageId;

    private LocalDate date;

    // 아티스트 공연은 타임테이블에서 묶음으로 표시되므로 상세에서는 time 미반환
    @JsonInclude(JsonInclude.Include.NON_NULL)
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

        // 아티스트 공연은 타임테이블에서 묶음으로 표시되므로 상세에서 개별 시간은 노출하지 않음
        String time = null;
        if (stage.getCategory() != StageCategory.ARTIST_PERFORMANCE) {
            String formattedStartAt = stage.getStartAt().format(TIME_FORMATTER);
            String formattedEndAt = stage.getEndAt().format(TIME_FORMATTER);
            time = formattedStartAt + " - " + formattedEndAt;
        }

        return StageInfoResponseDTO.builder()
                .stageId(stage.getId())
                .date(stage.getDate())
                .time(time)
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
