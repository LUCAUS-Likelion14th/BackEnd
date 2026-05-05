package com.example.lucaus26th.controller.booth;

import com.example.lucaus26th.dto.request.booth.BoothRequestDto;
import com.example.lucaus26th.dto.request.booth.BoothUpdateRequestDto;
import com.example.lucaus26th.dto.response.booth.BoothResponseDto;
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
    public ResponseEntity<BoothResponseDto.All> createBooth(@ModelAttribute BoothRequestDto request){
        BoothResponseDto.All response = boothService.createBooth(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping(value = "/{boothId}", consumes = "multipart/form-data")
    @Operation(summary = "부스 수정", description = "boothId에 해당하는 부스를 수정합니다.")
    public ResponseEntity<BoothResponseDto.All> updateBooth(@PathVariable Long boothId, @ModelAttribute BoothUpdateRequestDto request){
        BoothResponseDto.All response = boothService.updateBooth(boothId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @DeleteMapping("/{boothId}")
    public ResponseEntity<Void> deleteBooth(@PathVariable Long boothId){
        boothService.deleteBooth(boothId);
        return ResponseEntity.noContent().build();
    }


}
