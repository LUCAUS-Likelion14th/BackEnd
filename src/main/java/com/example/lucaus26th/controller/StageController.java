package com.example.lucaus26th.controller;

import com.example.lucaus26th.dto.response.PerformerSimpleResponseDTO;
import com.example.lucaus26th.global.api.ApiResponse;
import com.example.lucaus26th.service.StageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/stage")
@RequiredArgsConstructor
@Tag(name = "공연", description = "공연 관련 API")
public class StageController {
    private final StageService stageService;

    // 날짜별 공연 조회(상단)
    @GetMapping
    @Operation(summary = "날짜별 공연자 조회(상단)", description = "날짜, 카테고리별 공연자 간단 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<List<PerformerSimpleResponseDTO>>> getPerformerList(){
        return ResponseEntity.ok(ApiResponse.success(stageService.getPerformerList()));
    }
}
