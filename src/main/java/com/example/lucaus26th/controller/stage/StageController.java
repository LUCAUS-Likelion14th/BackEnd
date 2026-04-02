package com.example.lucaus26th.controller.stage;

import com.example.lucaus26th.enums.StageCategory;
import com.example.lucaus26th.dto.response.stage.PerformerSimpleResponseDTO;
import com.example.lucaus26th.dto.response.stage.StageInfoResponseDTO;
import com.example.lucaus26th.dto.response.stage.StageResponseDTO;
import com.example.lucaus26th.global.api.ApiResponse;
import com.example.lucaus26th.service.stage.StageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/stage")
@RequiredArgsConstructor
@Tag(name = "공연", description = "공연 관련 API")
public class StageController {
    private final StageService stageService;

    // 날짜별 공연 조회(상단)
    @GetMapping
    @Operation(summary = "공연 라인업 조회", description = "날짜, 카테고리별 공연자 간단 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<List<PerformerSimpleResponseDTO>>> getPerformerList(
            @Parameter(description = "공연 날짜 (yyyy-MM-dd)", example = "2026-05-14")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,

            @Parameter(description = "공연 카테고리", example = "ARTIST_PERFORMANCE")
            @RequestParam
            StageCategory category
    ){
        return ResponseEntity.ok(ApiResponse.success(stageService.getPerformerList(date, category)));
    }

    // 타임테이블 조회
    @GetMapping("/timetable")
    @Operation(summary = "공연 타임테이블 전체조회", description = "공연 페이지 하단 타임테이블을 조회합니다.")
    public ResponseEntity<ApiResponse<List<StageResponseDTO>>> getStageList(
            @Parameter(description = "공연 날짜", example = "2026-05-18")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ){
        return ResponseEntity.ok(ApiResponse.success(stageService.getStageList(date)));
    }

    // 공연 정보 상세 조회
    @GetMapping("/{stageId}")
    @Operation(summary = "공연 상세 조회", description = "stage_id에 해당하는 공연의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<StageInfoResponseDTO>> getStageDetail(
            @PathVariable Long stageId
    ){
        return ResponseEntity.ok(ApiResponse.success(stageService.getStageInfo(stageId)));
    }
}
