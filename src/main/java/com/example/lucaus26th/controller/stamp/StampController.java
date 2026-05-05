package com.example.lucaus26th.controller.stamp;

import com.example.lucaus26th.dto.request.stamp.StampRegisterRequestDto;
import com.example.lucaus26th.dto.request.stamp.StampRequestDto;
import com.example.lucaus26th.dto.response.stamp.StampMemberInfoResponseDto;
import com.example.lucaus26th.dto.response.stamp.StampResponseDto;
import com.example.lucaus26th.global.api.ApiResponse;
import com.example.lucaus26th.security.CustomUserDetails;
import com.example.lucaus26th.service.stamp.StampService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stamp")
@RequiredArgsConstructor
@Tag(name = "도장판" , description = "도장판 관련 API")
public class StampController {

    private final StampService stampService;

    @PatchMapping("/init")
    public ResponseEntity<ApiResponse<Void>> updateMemberInfo(@RequestBody StampRegisterRequestDto request, @AuthenticationPrincipal CustomUserDetails user){
        stampService.updateMemberInfo(request, user.getMember().getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/init")
    public ResponseEntity<ApiResponse<StampMemberInfoResponseDto.Info>> getMemberInfo(@AuthenticationPrincipal CustomUserDetails user){
        return ResponseEntity.ok(ApiResponse.success(stampService.getMemberInfo(user.getMember().getId())));
    }

    @GetMapping("/check")
    public ResponseEntity<ApiResponse<StampMemberInfoResponseDto.Check>> getMemberCheckInfo(@AuthenticationPrincipal CustomUserDetails user){
        return ResponseEntity.ok(ApiResponse.success(stampService.checkMemberInfo(user.getMember().getId())));
    }

    @PostMapping("/{boothId}")
    public ResponseEntity<ApiResponse<Void>> createStamp(@PathVariable Long boothId, @RequestBody StampRequestDto request, @AuthenticationPrincipal CustomUserDetails user){
        stampService.createStamp(user.getMember().getId(), boothId, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<StampResponseDto.Stamp>> getStamp(@AuthenticationPrincipal CustomUserDetails user){
        return ResponseEntity.ok(ApiResponse.success(stampService.getStamp(user.getMember().getId())));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<StampResponseDto.My>> getStampMy(@AuthenticationPrincipal CustomUserDetails user){
        StampResponseDto.My response = stampService.getMyStamp(user.getMember().getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/prize")
    public ResponseEntity<ApiResponse<Void>> updateIsApplied(@RequestBody StampRequestDto request, @AuthenticationPrincipal CustomUserDetails user){
        stampService.updateIsApplied(user.getMember().getId(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }


}
