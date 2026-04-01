package com.example.lucaus26th.controller;


import com.example.lucaus26th.dto.request.BoothRequestDto;
import com.example.lucaus26th.dto.request.BoothUpdateRequestDto;
import com.example.lucaus26th.dto.response.ApiResponse;
import com.example.lucaus26th.dto.response.BoothResponseDto;
import com.example.lucaus26th.security.CustomUserDetails;
import com.example.lucaus26th.service.BoothService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booth")
@RequiredArgsConstructor
@Slf4j
public class BoothController {

    private final BoothService boothService;

    @PostMapping
    public ResponseEntity<Long> createBooth(@RequestBody  BoothRequestDto request){
        Long boothId = boothService.createBooth(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(boothId);
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<BoothResponseDto.Lists>>> getBooth(@AuthenticationPrincipal CustomUserDetails userDetails){
        List<BoothResponseDto.Lists> response = boothService.getBooth(userDetails);
        return ResponseEntity.ok(ApiResponse.of(response));
    }
    @GetMapping("/{boothId}")
    public ResponseEntity<ApiResponse<BoothResponseDto.Detail>> getBoothDetail(@PathVariable Long boothId, @AuthenticationPrincipal CustomUserDetails userDetails){
        BoothResponseDto.Detail response = boothService.getBoothDetail(boothId, userDetails);
        return ResponseEntity.ok(ApiResponse.of(response));
    }
    @PatchMapping("/{boothId}")
    public ResponseEntity<Long> updateBooth(@PathVariable Long boothId, @RequestBody BoothUpdateRequestDto request){
        boothService.updateBooth(boothId, request);
        return ResponseEntity.status(HttpStatus.OK).body(boothId);
    }
    @DeleteMapping("/{boothId}")
    public ResponseEntity<Void> deleteBooth(@PathVariable Long boothId){
        boothService.deleteBooth(boothId);
        return ResponseEntity.noContent().build();
    }

    // 좋아요 관련
    @PostMapping("/{boothId}/like")
    public ResponseEntity<Void> createBoothLike(@PathVariable Long boothId, @AuthenticationPrincipal CustomUserDetails userDetails){
        // 서비스 호출
        boothService.createBoothLike(boothId, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @DeleteMapping("/{boothId}/like")
    public ResponseEntity<Void> deleteBoothLike(@PathVariable Long boothId,@AuthenticationPrincipal CustomUserDetails userDetails){

        boothService.deleteBoothLike(boothId, userDetails.getId());
        return ResponseEntity.noContent().build();
    }
}
