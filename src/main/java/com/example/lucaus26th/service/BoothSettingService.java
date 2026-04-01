package com.example.lucaus26th.service;

import com.example.lucaus26th.domain.booth.Booth;
import com.example.lucaus26th.domain.booth.BoothSetting;
import com.example.lucaus26th.dto.request.BoothSettingRequestDto;
import com.example.lucaus26th.dto.response.BoothSettingResponseDto;
import com.example.lucaus26th.repository.booth.BoothRepository;
import com.example.lucaus26th.repository.booth.BoothSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoothSettingService {

    private final BoothSettingRepository boothSettingRepository;
    private final BoothRepository boothRepository;

    // BoothSetting CRUD 기능
    public BoothSettingResponseDto createBoothSetting(BoothSettingRequestDto request){
        // Booth 불러오기
        Long boothId = request.getBoothId();
        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new IllegalArgumentException("존재하는 않는 부스입니다. : " + boothId));

        BoothSetting setting = BoothSetting.builder()
                .booth(booth)
                .date(request.getDate())
                .day(request.getDay())
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .build();

        boothSettingRepository.save(setting);

        return BoothSettingResponseDto.fromEntity(setting,booth);
    }

    public BoothSettingResponseDto updateBoothSetting(BoothSettingRequestDto request, Long settingId){
        BoothSetting boothSetting = boothSettingRepository.findById(settingId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 부스 운영정보 : " + settingId));
        Long boothId = request.getBoothId();
        if(boothId != null){
            Booth booth = boothRepository.findById(boothId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부스입니다. : " + boothId));
            boothSetting.update(request, booth);
            boothSettingRepository.save(boothSetting);
            return BoothSettingResponseDto.fromEntity(boothSetting,boothSetting.getBooth());
        }
        boothSetting.update(request,null);
        boothSettingRepository.save(boothSetting);
        return BoothSettingResponseDto.fromEntity(boothSetting,boothSetting.getBooth());
    }

    public void deleteBoothSetting(Long settingId){

        boothSettingRepository.deleteById(settingId);
    }

}
