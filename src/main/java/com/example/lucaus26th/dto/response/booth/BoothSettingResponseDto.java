package com.example.lucaus26th.dto.response.booth;

import com.example.lucaus26th.domain.booth.Booth;
import com.example.lucaus26th.domain.booth.BoothSetting;
import com.example.lucaus26th.enums.BoothLocation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoothSettingResponseDto {
    private Long boothId;
    private String boothName;
    private String locationId;
    private BoothLocation location;
    private LocalDate date;
    private String day;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startAt;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endAt;

    public static BoothSettingResponseDto fromEntity(BoothSetting setting, Booth booth){
        return  BoothSettingResponseDto.builder()
                .boothId(booth.getId())
                .boothName(booth.getName())
                .locationId(setting.getLocationId())
                .location(setting.getLocation())
                .date(setting.getDate())
                .day(setting.getDay())
                .startAt(setting.getStartAt())
                .endAt(setting.getEndAt())
                .build();
    }
}
