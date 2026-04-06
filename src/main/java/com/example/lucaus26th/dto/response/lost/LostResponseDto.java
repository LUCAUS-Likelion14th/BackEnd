package com.example.lucaus26th.dto.response.lost;


import com.example.lucaus26th.domain.lost.Lost;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LostResponseDto {

    @JsonProperty("lost_id")
    private Long lostId;
    private String name;
    private String image;
    private String date;
    @JsonProperty("find_location")
    private String findLocation;

    public static LostResponseDto fromEntity(Lost lost){
        return LostResponseDto.builder()
                .lostId(lost.getId())
                .name(lost.getName())
                .image(lost.getImage())
                .date(lost.getDate())
                .findLocation(lost.getFindLocation())
                .build();
    }

}
