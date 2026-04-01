package com.example.lucaus26th.controller;

import com.example.lucaus26th.dto.request.StageRequestDTO;
import com.example.lucaus26th.dto.response.StageResponseDTO;
import com.example.lucaus26th.global.api.ApiResponse;
import com.example.lucaus26th.service.StageAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stage/admin")
@RequiredArgsConstructor
@Tag(name = "공연 관리", description = "공연 등록/수정/삭제 관련 API")
public class StageAdminController {
    private final StageAdminService stageAdminService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "공연 생성", description = "새로운 공연을 생성합니다.")
    public ApiResponse<StageResponseDTO> createStage(@RequestBody StageRequestDTO request){
        return ApiResponse.success(stageAdminService.createStage(request));
    }

    @PatchMapping("/{stageId}")
    @Operation(summary = "공연 수정", description = "stageId에 해당하는 공연을 수정합니다.")
    public ApiResponse<StageResponseDTO> updateStage(
            @PathVariable("stageId") Long stageId,
            @RequestBody StageRequestDTO request){
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

}
