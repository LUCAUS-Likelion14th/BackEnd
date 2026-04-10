package com.example.lucaus26th.controller.booth;

import com.example.lucaus26th.dto.request.booth.BoothRequestDto;
import com.example.lucaus26th.dto.request.booth.BoothUpdateRequestDto;
import com.example.lucaus26th.service.booth.BoothService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/booth")
@RequiredArgsConstructor
@Tag(name = "부스 관리", description = "부스 등록/수정/삭제 관련 API")
public class BoothAdminController {
    private final BoothService boothService;

    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "부스 생성", description = "새로운 부스를 생성합니다.")
    public ResponseEntity<Long> createBooth(@ModelAttribute BoothRequestDto request){
        Long boothId = boothService.createBooth(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(boothId);
    }

    @PatchMapping(value = "/{boothId}", consumes = "multipart/form-data")
    @Operation(summary = "부스 수정", description = "boothId에 해당하는 부스를 수정합니다.")
    public ResponseEntity<Long> updateBooth(@PathVariable Long boothId, @ModelAttribute BoothUpdateRequestDto request){
        boothService.updateBooth(boothId, request);
        return ResponseEntity.status(HttpStatus.OK).body(boothId);
    }
    @DeleteMapping("/{boothId}")
    public ResponseEntity<Void> deleteBooth(@PathVariable Long boothId){
        boothService.deleteBooth(boothId);
        return ResponseEntity.noContent().build();
    }


}
