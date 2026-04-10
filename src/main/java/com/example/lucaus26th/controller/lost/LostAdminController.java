package com.example.lucaus26th.controller.lost;

import com.example.lucaus26th.dto.request.lost.LostRequestDto;
import com.example.lucaus26th.dto.response.lost.LostResponseDto;
import com.example.lucaus26th.global.api.ApiResponse;
import com.example.lucaus26th.security.CustomUserDetails;
import com.example.lucaus26th.service.lost.LostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/lost")
@RequiredArgsConstructor
@Tag(name = "분실물 관리", description = "분실물 관련 생성/수정/삭제 API")
public class LostAdminController {

    private final LostService lostService;

    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "분실물 생성",description = "새로운 분실물을 생성합니다.")
    public ResponseEntity<ApiResponse<LostResponseDto>> createLost(@Valid @ModelAttribute LostRequestDto request,
                                                                   @AuthenticationPrincipal CustomUserDetails userDetails){

        System.out.println("image: " + request.getImage());
        System.out.println("isEmpty: " + (request.getImage() == null ? "null" : request.getImage().isEmpty()));
        System.out.println("size: " + (request.getImage() == null ? "null" : request.getImage().getSize()));

        LostResponseDto response = lostService.createLost(request,userDetails);
        return ResponseEntity.ok(ApiResponse.success(response));

    }

    @PatchMapping(value = "/{lostId}", consumes = "multipart/form-data")
    @Operation(summary = "분실물 수정",description = "lostId에 해당하는 분실물을 수정합니다.")
    public ResponseEntity<ApiResponse<LostResponseDto>> updateLost(@PathVariable Long lostId, @Valid
    @ModelAttribute LostRequestDto request, @AuthenticationPrincipal CustomUserDetails userDetails){

        LostResponseDto response = lostService.updateLost(lostId,request,userDetails);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{lostId}")
    @Operation(summary = "분실물 삭제",description = "lostId에 해당하는 분실물을 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> deleteLost(@PathVariable Long lostId, @AuthenticationPrincipal CustomUserDetails userDetails){
        lostService.deleteLost(lostId,userDetails);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
