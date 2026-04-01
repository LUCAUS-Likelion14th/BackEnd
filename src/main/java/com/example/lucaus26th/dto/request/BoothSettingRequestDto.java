package com.example.lucaus26th.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class BoothSettingRequestDto {

    private Long boothId;
    private LocalDate date;
    private String day;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startAt;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endAt;

}
