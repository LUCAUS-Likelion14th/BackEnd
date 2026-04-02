package com.example.lucaus26th.dto.response;

import com.example.lucaus26th.domain.Setting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SettingResponseDto {
    private String mon;
    private String tue;
    private String wed;
    private String thu;
    private String fri;

    public static SettingResponseDto fromEntity(Setting setting){
        return  SettingResponseDto.builder()
                .mon(setting.getMon())
                .tue(setting.getTue())
                .wed(setting.getWed())
                .thu(setting.getThu())
                .fri(setting.getFri())
                .build();
    }
}
