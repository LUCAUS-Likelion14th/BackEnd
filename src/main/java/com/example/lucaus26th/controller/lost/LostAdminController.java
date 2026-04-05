package com.example.lucaus26th.controller.lost;

import com.example.lucaus26th.dto.request.lost.LostRequestDto;
import com.example.lucaus26th.dto.response.ApiResponse;
import com.example.lucaus26th.dto.response.lost.LostResponseDto;
import com.example.lucaus26th.security.CustomUserDetails;
import com.example.lucaus26th.service.lost.LostService;
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
@Tag(name = "분실물 관리", description = "분실물 관련 관리자 API")
public class LostAdminController {

    private final LostService lostService;

    @PostMapping
    public ResponseEntity<ApiResponse<LostResponseDto>> createLost(@Valid @RequestBody LostRequestDto request, @AuthenticationPrincipal CustomUserDetails userDetails){

        LostResponseDto response = lostService.createLost(request,userDetails);
        return ResponseEntity.ok(ApiResponse.of(response));

    }

    @PatchMapping("/{lostId}")
    public ResponseEntity<ApiResponse<LostResponseDto>> updateLost(@PathVariable Long lostId, @Valid @RequestBody LostRequestDto request, @AuthenticationPrincipal CustomUserDetails userDetails){

        LostResponseDto response = lostService.updateLost(lostId,request,userDetails);
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @DeleteMapping("/{lostId}")
    public ResponseEntity<Void> deleteLost(@PathVariable Long lostId, @AuthenticationPrincipal CustomUserDetails userDetails){
        lostService.deleteLost(lostId,userDetails);
        return ResponseEntity.noContent().build();
    }

}
