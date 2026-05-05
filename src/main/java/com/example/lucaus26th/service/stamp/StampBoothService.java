package com.example.lucaus26th.service.stamp;

import com.example.lucaus26th.domain.booth.Booth;
import com.example.lucaus26th.domain.stamp.StampBooth;
import com.example.lucaus26th.repository.booth.BoothRepository;
import com.example.lucaus26th.repository.stamp.StampBoothRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class StampBoothService {
    private final StampBoothRepository stampBoothRepository;
    private final BoothRepository boothRepository;

    public Long createStampBooth(Long boothId){
        // 이미 해당 부스가 스탬프 대상으로 등록되어 있는지 확인
        if (stampBoothRepository.existsByBoothId(boothId)) {
            throw new IllegalStateException("이미 스탬프 대상 부스로 등록되어 있습니다.");
        }

        // 부스 존재 여부 확인 및 조회
        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new IllegalArgumentException("해당 부스가 존재하지 않습니다. ID: " + boothId));

        StampBooth stampBooth = StampBooth.builder()
                .booth(booth)
                .build();

        return stampBoothRepository.save(stampBooth).getId();
    }

    public void deleteStampBooth(Long stampBoothId){
        stampBoothRepository.deleteById(stampBoothId);
    }
}
