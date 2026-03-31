package com.example.lucaus26th.service;

import com.example.lucaus26th.domain.Setting;
import com.example.lucaus26th.dto.request.SettingRequestDto;
import com.example.lucaus26th.dto.response.SettingResponseDto;
import com.example.lucaus26th.repository.booth.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SettingService {
    private final SettingRepository settingRepository;

    public SettingResponseDto createSetting(SettingRequestDto request){
        Setting setting = Setting.builder()
                .mon(request.getMon())
                .tue(request.getTue())
                .wed(request.getWed())
                .thu(request.getThu())
                .fri(request.getFri())
                .build();

        settingRepository.save(setting);
        return SettingResponseDto.fromEntity(setting);
    }

    public SettingResponseDto updateSetting(Long settingId, SettingRequestDto request) {
        // 추후에 관리자 체크할것

        Setting setting = settingRepository.findById(settingId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 운영정보입니다." + settingId));

        setting.update(request);
        return SettingResponseDto.fromEntity(setting);
    }

    public void deleteSetting(Long settingId) {

        Setting setting = settingRepository.findById(settingId)
                        .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 운영정보입니다." + settingId));

        settingRepository.deleteById(settingId);
    }
}
