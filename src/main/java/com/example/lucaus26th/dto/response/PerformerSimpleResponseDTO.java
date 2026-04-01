package com.example.lucaus26th.dto.response;

import com.example.lucaus26th.domain.Stage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PerformerSimpleResponseDTO {
    @JsonProperty("stage_id")
    private Long stageId;
    private String performer;
    private String logo;

    public static PerformerSimpleResponseDTO from(Stage stage){
        return PerformerSimpleResponseDTO.builder()
                .stageId(stage.getId())
                .performer(stage.getPerformer())
                .logo(stage.getLogo())
                .build();
    }
}
