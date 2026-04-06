package com.example.lucaus26th.controller;

import com.example.lucaus26th.dto.request.PromotionRequestDTO;
import com.example.lucaus26th.dto.response.PromotionResponseDTO;
import com.example.lucaus26th.global.api.ApiResponse;
import com.example.lucaus26th.service.PromotionAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/promotion")
@RequiredArgsConstructor
@Tag(name = "프로모션 관리", description = "프로모션 생성/수정/삭제 관련 API")
public class PromotionAdminController {

    private final PromotionAdminService promotionAdminService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "프로모션 생성", description = "새로운 프로모션을 생성합니다.")
    public ApiResponse<PromotionResponseDTO> createPromotion(@RequestBody PromotionRequestDTO request){
        return ApiResponse.success(promotionAdminService.createPromotion(request));
    }

    @PatchMapping("/{promotionId}")
    @Operation(summary = "프로모션 수정", description = "promotionId에 해당하는 프로모션을 수정합니다.")
    public ApiResponse<PromotionResponseDTO> updatePromotion(
            @PathVariable("promotionId") Long promotionId,
            @RequestBody PromotionRequestDTO request){
        return ApiResponse.success(promotionAdminService.updatePromotion(promotionId, request));
    }

    @DeleteMapping("/{promotionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "프로모션 삭제", description = "promotionId에 해당하는 프로모션을 삭제합니다.")
    public void deletePromotion(@PathVariable("promotionId") Long promotionId){
        promotionAdminService.deletePromotion(promotionId);
    }
}
