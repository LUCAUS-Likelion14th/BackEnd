package com.example.lucaus26th.service.stage;

import com.example.lucaus26th.domain.stage.Stage;
import com.example.lucaus26th.enums.StageCategory;
import com.example.lucaus26th.dto.response.stage.LiveStageResponseDTO;
import com.example.lucaus26th.dto.response.stage.PerformerSimpleResponseDTO;
import com.example.lucaus26th.dto.response.stage.StageInfoResponseDTO;
import com.example.lucaus26th.dto.response.stage.StageResponseDTO;
import com.example.lucaus26th.repository.stage.StageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StageService {

    private final StageRepository stageRepository;

    // 공연자 목록 조회
    @Cacheable(value = "performance", key = "'performers_' + #date + '_' + #category")
    public List<PerformerSimpleResponseDTO> getPerformerList(LocalDate date, StageCategory category) {
        return stageRepository.findByDateAndCategoryOrderByStartAtAsc(date, category).stream()
                .map(PerformerSimpleResponseDTO::from)
                .toList();
    }

    // 타임테이블 조회
    @Cacheable(value = "performance", key = "'stage_list_' + #date")
    public List<StageResponseDTO> getStageList(LocalDate date) {
        return stageRepository.findByDateOrderByStartAtAsc(date).stream()
                .map(StageResponseDTO::from)
                .toList();
    }

    // 공연 정보 상세 조회
    @Cacheable(value = "performance", key = "'stage_info_' + #stageId")
    public StageInfoResponseDTO getStageInfo(Long stageId) {
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new IllegalArgumentException("Stage not found"));
        return StageInfoResponseDTO.from(stage);
    }

    // 메인 홈 실시간 공연 조회
    public LiveStageResponseDTO getLiveStage() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        Stage stage = stageRepository
                .findByDateAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
                        today, now, now
                ).orElseThrow(() -> new IllegalArgumentException("Stage not found"));


        return LiveStageResponseDTO.from(stage);
    }
}
