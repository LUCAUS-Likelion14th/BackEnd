package com.example.lucaus26th.controller;

import com.example.lucaus26th.dto.request.BoothSettingRequestDto;
import com.example.lucaus26th.dto.response.ApiResponse;
import com.example.lucaus26th.dto.response.BoothSettingResponseDto;
import com.example.lucaus26th.service.BoothSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booth/setting")
@RequiredArgsConstructor
public class BoothSettingController {

    private final BoothSettingService boothSettingService;

    @PostMapping
    public ResponseEntity<BoothSettingResponseDto> createBoothSetting(@RequestBody BoothSettingRequestDto request) {
        BoothSettingResponseDto response = boothSettingService.createBoothSetting(request);
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{settingId}")
    public ResponseEntity<BoothSettingResponseDto> updateBoothSetting(@PathVariable Long settingId, @RequestBody BoothSettingRequestDto request) {
        BoothSettingResponseDto response = boothSettingService.updateBoothSetting(request, settingId);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{settingId}")
    public ResponseEntity<Void> deleteBoothSetting(@PathVariable Long settingId) {
        boothSettingService.deleteBoothSetting(settingId);
        return ResponseEntity.noContent().build();
    }
}
