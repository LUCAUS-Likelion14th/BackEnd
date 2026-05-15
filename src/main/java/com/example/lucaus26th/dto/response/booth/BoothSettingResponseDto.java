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
    private String locationId;
    private String location;
    private LocalDate date;
    private String day;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startAt;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endAt;

    public static BoothSettingResponseDto fromEntity(BoothSetting setting){

        return BoothSettingResponseDto.builder()
                .locationId(setting.getLocationId())
                .location(setting.getLocation().getDescription())
                .date(setting.getDate())
                .day(setting.getDay())
                .startAt(setting.getStartAt())
                .endAt(setting.getEndAt())
                .build();
    }
}
