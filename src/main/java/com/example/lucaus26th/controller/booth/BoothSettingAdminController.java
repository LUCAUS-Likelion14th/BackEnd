package com.example.lucaus26th.controller.booth;

import com.example.lucaus26th.dto.request.booth.BoothSettingRequestDto;
import com.example.lucaus26th.dto.response.booth.BoothSettingResponseDto;
import com.example.lucaus26th.service.booth.BoothSettingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/booth/setting")
@RequiredArgsConstructor
@Tag(name = "부스 운영정보 관리", description = "부스 운영정보 등록/수정/삭제 API")
public class BoothSettingAdminController {

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
