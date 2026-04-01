package com.example.lucaus26th.service;

import com.example.lucaus26th.domain.Stage;
import com.example.lucaus26th.domain.StageInfo;
import com.example.lucaus26th.dto.request.StageRequestDTO;
import com.example.lucaus26th.dto.response.StageResponseDTO;
import com.example.lucaus26th.repository.StageInfoRepository;
import com.example.lucaus26th.repository.StageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StageAdminService {

    private final StageRepository stageRepository;
    private final StageInfoRepository stageInfoRepository;

    // 공연 생성
    @Transactional
    public StageResponseDTO createStage(StageRequestDTO request) {
        Stage stage = Stage.create(
                request.getCategory(),
                request.getStartAt(),
                request.getEndAt(),
                request.getDate(),
                request.getPerformer(),
                request.getLogo()
        );

        Stage savedStage = stageRepository.save(stage);

        if(request.hasStageInfoField()){
            StageInfo stageInfo = StageInfo.create(
                    savedStage,
                    request.getInstagram(),
                    request.getYoutube(),
                    request.getPerformerImage(),
                    request.getInfo()
            );
            stageInfoRepository.save(stageInfo);
            savedStage.connectStageInfo(stageInfo);
        }
        return StageResponseDTO.from(savedStage);
    }

    @Transactional
    public StageResponseDTO updateStage(Long stageId, StageRequestDTO request){
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new EntityNotFoundException("no stage found with id: " + stageId));

        stage.updateStage(
                request.getCategory(),
                request.getStartAt(),
                request.getEndAt(),
                request.getDate(),
                request.getPerformer(),
                request.getLogo()
        );

        StageInfo stageInfo = stageInfoRepository.findById(stageId).orElse(null);

        if(request.hasStageInfoField()){
            if(stageInfo == null){
                stageInfo = StageInfo.create(
                        stage,
                        request.getInstagram(),
                        request.getYoutube(),
                        request.getPerformerImage(),
                        request.getInfo()
                );
                stageInfoRepository.save(stageInfo);
                stage.connectStageInfo(stageInfo);
            }else{
                stageInfo.update(
                        request.getInstagram(),
                        request.getYoutube(),
                        request.getPerformerImage(),
                        request.getInfo()
                );
            }
        }

        return StageResponseDTO.from(stage);

    }

    @Transactional
    public void deleteStage(Long stageId) {
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new EntityNotFoundException("no stage found with id: " + stageId));
        stageRepository.delete(stage);
    }

    @Transactional
    public void deleteStageInfo(Long stageId) {
        StageInfo stageInfo = stageInfoRepository.findById(stageId)
                .orElseThrow(() -> new EntityNotFoundException("no stage found with id: " + stageId));
        Stage stage = stageInfo.getStage();
        stage.disconnectStageInfo();
        stageInfoRepository.delete(stageInfo);
    }
}
