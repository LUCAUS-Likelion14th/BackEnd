package com.example.lucaus26th.service.stage;

import com.example.lucaus26th.domain.stage.Stage;
import com.example.lucaus26th.enums.StageCategory;
import com.example.lucaus26th.enums.StageEndpoint;
import com.example.lucaus26th.dto.response.stage.LiveStageResponseDTO;
import com.example.lucaus26th.dto.response.stage.PerformerSimpleResponseDTO;
import com.example.lucaus26th.dto.response.stage.StageInfoResponseDTO;
import com.example.lucaus26th.dto.response.stage.StageResponseDTO;
import com.example.lucaus26th.global.exception.BusinessException;
import com.example.lucaus26th.global.exception.ErrorCode;
import com.example.lucaus26th.repository.stage.StageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StageService {

    private final StageRepository stageRepository;

    @Value("${stage.artist-group-logo-url:}")
    private String artistGroupLogoUrl;

    // 공연자 목록 조회
    // 카테고리 기본 노출이 LINEUP을 포함하지 않거나, visibility=TIMETABLE_ONLY 인 항목은 제외
    @Cacheable(value = "performance", key = "'performers_' + #date + '_' + #category")
    public List<PerformerSimpleResponseDTO> getPerformerList(LocalDate date, StageCategory category) {
        return stageRepository.findByDateAndCategoryOrderByStartAtAsc(date, category).stream()
                .filter(stage -> stage.isVisibleAt(StageEndpoint.LINEUP))
                .map(PerformerSimpleResponseDTO::from)
                .toList();
    }

    // 타임테이블 조회
    // - 카테고리 기본 노출이 TIMETABLE을 포함하지 않는 항목은 제외 (현 카테고리상 해당 없음)
    // - 연속된 아티스트 공연(앞 공연의 endAt == 다음 공연의 startAt)은 하나의 묶음 응답으로 합쳐서 반환함
    @Cacheable(value = "performance", key = "'stage_list_' + #date")
    public List<StageResponseDTO> getStageList(LocalDate date) {
        List<Stage> stages = stageRepository.findByDateOrderByStartAtAsc(date).stream()
                .filter(stage -> stage.isVisibleAt(StageEndpoint.TIMETABLE))
                .toList();
        List<StageResponseDTO> result = new ArrayList<>();
        List<Stage> artistGroup = new ArrayList<>();

        for (Stage stage : stages) {
            if (stage.getCategory() == StageCategory.ARTIST_PERFORMANCE) { // 아티스트 공연인 경우
                if (!artistGroup.isEmpty() // 새로운 아티스트 공연 묶음 생성하는 경우
                        && !artistGroup.get(artistGroup.size() - 1).getEndAt().equals(stage.getStartAt())) {
                    result.add(StageResponseDTO.fromArtistGroup(artistGroup, artistGroupLogoUrl)); // dto에 이미 생성된 묶음 추가
                    artistGroup = new ArrayList<>();
                }
                artistGroup.add(stage);
            } else {
                if (!artistGroup.isEmpty()) {
                    result.add(StageResponseDTO.fromArtistGroup(artistGroup, artistGroupLogoUrl));
                    artistGroup = new ArrayList<>();
                }
                result.add(StageResponseDTO.from(stage));
            }
        }

        if (!artistGroup.isEmpty()) {
            result.add(StageResponseDTO.fromArtistGroup(artistGroup, artistGroupLogoUrl));
        }

        return result;
    }

    // 공연 정보 상세 조회 — 라인업과 동일한 노출 규칙 적용
    @Cacheable(value = "performance", key = "'stage_info_' + #stageId")
    public StageInfoResponseDTO getStageInfo(Long stageId) {
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STAGE_NOT_FOUND));
        if (!stage.isVisibleAt(StageEndpoint.LINEUP)) {
            throw new BusinessException(ErrorCode.STAGE_NOT_FOUND);
        }
        return StageInfoResponseDTO.from(stage);
    }

    // 메인 홈 실시간 공연 조회
    // 카테고리 기본 노출이 LIVE를 포함하지 않거나, visibility=TIMETABLE_ONLY 인 항목은 제외
    public LiveStageResponseDTO getLiveStage() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        return stageRepository.findByDateOrderByStartAtAsc(today).stream()
                .filter(stage -> stage.isVisibleAt(StageEndpoint.LIVE))
                .filter(stage -> !stage.getStartAt().isAfter(now) && stage.getEndAt().isAfter(now))
                .findFirst()
                .map(LiveStageResponseDTO::from)
                .orElse(null);
    }
}
