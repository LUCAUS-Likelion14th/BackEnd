package com.example.lucaus26th.controller.stage;

import com.example.lucaus26th.dto.request.stage.SongRequestDTO;
import com.example.lucaus26th.dto.request.stage.StageRequestDTO;
import com.example.lucaus26th.dto.response.stage.SongResponseDTO;
import com.example.lucaus26th.dto.response.stage.StageResponseDTO;
import com.example.lucaus26th.global.api.ApiResponse;
import com.example.lucaus26th.service.stage.StageAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/stage")
@RequiredArgsConstructor
@Tag(name = "공연 관리", description = "공연 등록/수정/삭제 관련 API")
public class StageAdminController {
    private final StageAdminService stageAdminService;

    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "공연 생성", description = "새로운 공연을 생성합니다.")
    public ApiResponse<StageResponseDTO> createStage(@ModelAttribute StageRequestDTO request){
        return ApiResponse.success(stageAdminService.createStage(request));
    }

    @PatchMapping(value = "/{stageId}", consumes = "multipart/form-data")
    @Operation(summary = "공연 수정", description = "stageId에 해당하는 공연을 수정합니다.")
    public ApiResponse<StageResponseDTO> updateStage(
            @PathVariable("stageId") Long stageId,
            @ModelAttribute StageRequestDTO request){
        return ApiResponse.success(stageAdminService.updateStage(stageId, request));
    }

    @DeleteMapping("/{stageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "공연 삭제", description = "stageId에 해당하는 공연을 삭제합니다.")
    public void deleteStage(@PathVariable("stageId") Long stageId){
        stageAdminService.deleteStage(stageId);
    }

    @DeleteMapping("/{stageId}/info")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "공연 정보 삭제", description = "stageId에 해당하는 공연 상세정보를 삭제합니다.")
    public void deleteStageInfo(@PathVariable("stageId") Long stageId){
        stageAdminService.deleteStageInfo(stageId);
    }

    @PostMapping("/{stageId}/song")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "곡 생성", description = "stageId에 해당하는 공연의 곡을 추가합니다.")
    public ApiResponse<SongResponseDTO> createSong(
            @PathVariable("stageId") Long stageId,
            @RequestBody SongRequestDTO request){
        return ApiResponse.success(stageAdminService.createSong(stageId, request));
    }

    @PatchMapping("/{stageId}/song/{songId}")
    @Operation(summary = "곡 수정", description = "songId에 해당하는 곡을 수정합니다.")
    public ApiResponse<SongResponseDTO> updateSong(
            @PathVariable("stageId") Long stageId,
            @PathVariable("songId") Long songId,
            @RequestBody SongRequestDTO request){
        return ApiResponse.success(stageAdminService.updateSong(stageId, songId, request));
    }

    @DeleteMapping("/{stageId}/song/{songId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "곡 삭제", description = "songId에 해당하는 곡을 삭제합니다.")
    public void deleteSong(
            @PathVariable("stageId") Long stageId,
            @PathVariable("songId") Long songId
    ){
        stageAdminService.deleteSong(stageId, songId);
    }









}
