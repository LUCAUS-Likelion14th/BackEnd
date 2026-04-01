package com.example.lucaus26th.service;

import com.example.lucaus26th.domain.StageCategory;
import com.example.lucaus26th.dto.response.PerformerSimpleResponseDTO;
import com.example.lucaus26th.dto.response.StageResponseDTO;
import com.example.lucaus26th.repository.StageRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StageService {

    private final StageRepository stageRepository;

    // 공연자 목록 조회
    public List<PerformerSimpleResponseDTO> getPerformerList(LocalDate date, StageCategory category) {
        return stageRepository.findByDateAndCategoryOrderByStartAtAsc(date, category).stream()
                .map(PerformerSimpleResponseDTO::from)
                .toList();
    }

    // 타임테이블 조회
    public List<StageResponseDTO> getStageList(LocalDate date) {
        return stageRepository.findByDateOrderByStartAtAsc(date).stream()
                .map(StageResponseDTO::from)
                .toList();
    }
}
