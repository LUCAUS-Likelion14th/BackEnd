package com.example.lucaus26th.service.stage;

import com.example.lucaus26th.domain.Promotion;
import com.example.lucaus26th.domain.stage.Song;
import com.example.lucaus26th.domain.stage.Stage;
import com.example.lucaus26th.domain.stage.StageInfo;
import com.example.lucaus26th.dto.request.stage.SongRequestDTO;
import com.example.lucaus26th.dto.request.stage.StageRequestDTO;
import com.example.lucaus26th.dto.response.stage.SongResponseDTO;
import com.example.lucaus26th.dto.response.stage.StageResponseDTO;
import com.example.lucaus26th.global.S3Service;
import com.example.lucaus26th.repository.stage.SongRepository;
import com.example.lucaus26th.repository.stage.StageInfoRepository;
import com.example.lucaus26th.repository.stage.StageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StageAdminService {

    private final StageRepository stageRepository;
    private final StageInfoRepository stageInfoRepository;
    private final SongRepository songRepository;
    private final S3Service s3Service;

    // 공연 생성
    @Transactional
    public StageResponseDTO createStage(StageRequestDTO request) {
        String logoImageUrl;
        try{
            logoImageUrl = s3Service.upload(request.getLogoImage(), "stage/logo");
        } catch(IOException e){
            throw new RuntimeException("S3 이미지 업로드에 실패했습니다.", e);
        }
        Stage stage = Stage.create(
                request.getCategory(),
                request.getStartAt(),
                request.getEndAt(),
                request.getDate(),
                request.getPerformer(),
                logoImageUrl
        );

        Stage savedStage = stageRepository.save(stage);

        if(request.hasStageInfoField()){
            String performerImageUrl;
            try{
                performerImageUrl = s3Service.upload(request.getPerformerImage(), "stage/performer");
            } catch(IOException e){
                throw new RuntimeException("S3 이미지 업로드에 실패했습니다.", e);
            }

            StageInfo stageInfo = StageInfo.create(
                    request.getInstagram(),
                    request.getYoutube(),
                    performerImageUrl,
                    request.getInfo()
            );
            savedStage.connectStageInfo(stageInfo);
            stageInfoRepository.save(stageInfo);
        }
        return StageResponseDTO.from(savedStage);
    }

    @Transactional
    public StageResponseDTO updateStage(Long stageId, StageRequestDTO request){
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new EntityNotFoundException("no stage found with id: " + stageId));

        String logoImageUrl;
        try{
            logoImageUrl = s3Service.upload(request.getLogoImage(), "stage/logo");
        } catch(IOException e){
            throw new RuntimeException("S3 이미지 업로드에 실패했습니다", e);
        }
        stage.updateStage(
                request.getCategory(),
                request.getStartAt(),
                request.getEndAt(),
                request.getDate(),
                request.getPerformer(),
                logoImageUrl
        );

        StageInfo stageInfo = stageInfoRepository.findById(stageId).orElse(null);

        if(request.hasStageInfoField()){
            String performerImageUrl;
            try{
                performerImageUrl = s3Service.upload(request.getPerformerImage(), "stage/performer");
            }catch(IOException e){
                throw new RuntimeException("S3 이미지 업로드에 실패했습니다.", e);
            }
            if(stageInfo == null){
                stageInfo = StageInfo.create(
                        request.getInstagram(),
                        request.getYoutube(),
                        performerImageUrl,
                        request.getInfo()
                );
                stageInfoRepository.save(stageInfo);
                stage.connectStageInfo(stageInfo);
            }else{
                stageInfo.update(
                        request.getInstagram(),
                        request.getYoutube(),
                        performerImageUrl,
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

    @Transactional
    public SongResponseDTO createSong(Long stageId, SongRequestDTO request) {
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new EntityNotFoundException("no stage found with id: " + stageId));
        Song song = Song.create(request.getTitle(), request.getPlayOrder());
        Song savedSong = songRepository.save(song);
        stage.addSong(savedSong);
        return SongResponseDTO.from(savedSong);
    }

    @Transactional
    public SongResponseDTO updateSong(Long stageId, Long songId, SongRequestDTO request) {
        Song song = songRepository.findByIdAndStageId(songId, stageId)
                .orElseThrow(() -> new EntityNotFoundException("no song found with id: " + songId));
        song.updateSong(request.getTitle(), request.getPlayOrder());
        return SongResponseDTO.from(song);
    }

    @Transactional
    public void deleteSong(Long stageId, Long songId) {
        Song song = songRepository.findByIdAndStageId(songId, stageId)
                .orElseThrow(() -> new EntityNotFoundException("no song found with id: " + songId));
        songRepository.delete(song);
    }
}
