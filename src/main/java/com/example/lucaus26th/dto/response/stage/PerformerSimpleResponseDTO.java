package com.example.lucaus26th.dto.response.stage;

import com.example.lucaus26th.domain.stage.Stage;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonPropertyOrder({ "stage_id", "performer", "logo" })
public class PerformerSimpleResponseDTO {
    @JsonProperty("stage_id")
    private Long stageId;
    private String performer;
    private String logoImage;

    public static PerformerSimpleResponseDTO from(Stage stage){
        return PerformerSimpleResponseDTO.builder()
                .stageId(stage.getId())
                .performer(stage.getPerformer())
                .logoImage(stage.getLogoImage())
                .build();
    }
}
