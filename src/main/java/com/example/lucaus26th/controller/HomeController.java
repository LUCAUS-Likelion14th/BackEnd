package com.example.lucaus26th.controller;

import com.example.lucaus26th.dto.response.booth.BoothResponseDto;
import com.example.lucaus26th.dto.response.stage.LiveStageResponseDTO;
import com.example.lucaus26th.global.api.ApiResponse;
import com.example.lucaus26th.security.CustomUserDetails;
import com.example.lucaus26th.service.booth.BoothService;
import com.example.lucaus26th.service.stage.StageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "메인 홈", description = "메인 홈에 사용할 API입니다.")
public class HomeController {
    private final StageService stageService;
    private final BoothService boothService;

    @GetMapping("/live-stage")
    @Operation(summary = "실시간 공연 조회", description = "메인홈에서 현재 진행 중인 공연을 조회합니다.")
    public ResponseEntity<ApiResponse<LiveStageResponseDTO>> getLiveStage() {
        return ResponseEntity.ok(ApiResponse.success(stageService.getLiveStage()));
    }

    @GetMapping("/top-booth")
    @Operation(summary = "인기 부스 조회", description = "메인홈에서 현재 인기있는 부스를 조회합니다.")
    public ResponseEntity<ApiResponse<List<BoothResponseDto.Hot>>> getTopBooth(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<BoothResponseDto.Hot> response = boothService.getBoothHot(userDetails);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
