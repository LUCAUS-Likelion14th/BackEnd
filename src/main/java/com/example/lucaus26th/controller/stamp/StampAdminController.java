package com.example.lucaus26th.controller.stamp;

import com.example.lucaus26th.service.stamp.StampBoothService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/stamp")
@RequiredArgsConstructor
@Tag(name = "도장판 찍을 부스 관리", description = "도장판 찍을 부스 등록/삭제 관련 API")
public class StampAdminController {
    private final StampBoothService stampBoothService;

    @PostMapping("/{boothId}")
    public ResponseEntity<Long> createStampBooth(@PathVariable Long boothId){
        stampBoothService.createStampBooth(boothId);
        return ResponseEntity.ok(boothId);
    }

    @DeleteMapping("/{stampBoothId}")
    public ResponseEntity<Void> deleteBooth(@PathVariable Long stampBoothId){
        stampBoothService.deleteStampBooth(stampBoothId);
        return ResponseEntity.noContent().build();
    }
}
