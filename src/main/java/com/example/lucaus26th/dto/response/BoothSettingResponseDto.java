package com.example.lucaus26th.dto.response;

import com.example.lucaus26th.domain.Setting;
import com.example.lucaus26th.domain.booth.Booth;
import com.example.lucaus26th.domain.booth.BoothSetting;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@AllArgsConstructor
public class BoothSettingResponseDto {
    private Long boothId;
    private String boothName;
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
                .date(setting.getDate())
                .day(setting.getDay())
                .startAt(setting.getStartAt())
                .endAt(setting.getEndAt())
                .build();
    }
}
