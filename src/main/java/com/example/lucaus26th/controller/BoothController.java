package com.example.lucaus26th.controller;


import com.example.lucaus26th.dto.request.BoothRequestDto;
import com.example.lucaus26th.dto.request.BoothUpdateRequestDto;
import com.example.lucaus26th.service.BoothService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booth/")
@RequiredArgsConstructor
public class BoothController {

    private final BoothService boothService;

    @PostMapping
    public ResponseEntity<Long> createBooth(@RequestBody  BoothRequestDto request){
        Long boothId = boothService.createBooth(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(boothId);
    }
    @PatchMapping("/{boothId}/")
    public ResponseEntity<Long> updateBooth(@PathVariable Long boothId, @RequestBody BoothUpdateRequestDto request){
        boothService.updateBooth(boothId, request);
        return ResponseEntity.status(HttpStatus.OK).body(boothId);
    }
    @DeleteMapping("/{boothId}/")
    public ResponseEntity<Void> deleteBooth(@PathVariable Long boothId){
        boothService.deleteBooth(boothId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    // 좋아요 관련
    @PostMapping("/{boothId}/like/")
    public ResponseEntity<Void> createBoothLike(@PathVariable Long boothId){
        // user 정보 꺼냄
        Long memberId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 서비스 호출
        boothService.createBoothLike(boothId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @DeleteMapping("/{boothId}/like/")
    public ResponseEntity<Void> deleteBoothLike(@PathVariable Long boothId){
        Long memberId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boothService.deleteBoothLike(boothId, memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
