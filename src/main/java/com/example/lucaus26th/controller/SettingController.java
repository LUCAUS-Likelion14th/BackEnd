package com.example.lucaus26th.controller;


import com.example.lucaus26th.dto.request.SettingRequestDto;
import com.example.lucaus26th.dto.response.SettingResponseDto;
import com.example.lucaus26th.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/*
@RestController
@RequestMapping("/setting")
@RequiredArgsConstructor
public class SettingController {

    private final SettingService settingService;

    @PostMapping
    public ResponseEntity<SettingResponseDto> createSetting(@RequestBody SettingRequestDto request){
        SettingResponseDto response = settingService.createSetting(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{settingId}")
    public ResponseEntity<SettingResponseDto> updateSetting(@RequestBody SettingRequestDto request, @PathVariable Long settingId){
        SettingResponseDto response = settingService.updateSetting(settingId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{settingId}")
    public ResponseEntity<Void> deleteSetting(@PathVariable Long settingId){
        settingService.deleteSetting(settingId);
        return ResponseEntity.noContent().build();
    }
}*/
