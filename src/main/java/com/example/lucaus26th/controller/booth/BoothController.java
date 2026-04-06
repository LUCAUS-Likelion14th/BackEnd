package com.example.lucaus26th.controller.booth;


import com.example.lucaus26th.dto.request.booth.BoothRequestDto;
import com.example.lucaus26th.dto.request.booth.BoothUpdateRequestDto;
import com.example.lucaus26th.dto.response.booth.BoothResponseDto;
import com.example.lucaus26th.global.api.ApiResponse;
import com.example.lucaus26th.security.CustomUserDetails;
import com.example.lucaus26th.service.booth.BoothService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "부스", description = "부스 관련 API")
public class BoothController {

    private final BoothService boothService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BoothResponseDto.Lists>>> getBooth(@RequestParam(required = false) String date,
                                                                              @RequestParam(required = false) String location,
                                                                              @RequestParam(required = false) String category,
                                                                              @RequestParam(required = false) String search,
                                                                              @RequestParam(defaultValue = "0") int page,
                                                                              @AuthenticationPrincipal CustomUserDetails userDetails){
        Pageable pageable = PageRequest.of(page,8);
        Page<BoothResponseDto.Lists> response = boothService.getBooth(date, location, category,search, pageable,userDetails);
        //return ResponseEntity.ok(ApiResponse.of(response)); // 만약 메타데이터 필요없으면 response.getContent()
        return ResponseEntity.ok(ApiResponse.success(response.getContent()));
    }
    @GetMapping("/{boothId}")
    public ResponseEntity<ApiResponse<BoothResponseDto.Detail>> getBoothDetail(@PathVariable Long boothId, @AuthenticationPrincipal CustomUserDetails userDetails){
        BoothResponseDto.Detail response = boothService.getBoothDetail(boothId, userDetails);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 좋아요 관련
    @PostMapping("/{boothId}/like")
    public ResponseEntity<ApiResponse<Void>> createBoothLike(@PathVariable Long boothId, @AuthenticationPrincipal CustomUserDetails userDetails){
        // 서비스 호출
        boothService.createBoothLike(boothId, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
    @DeleteMapping("/{boothId}/like")
    public ResponseEntity<ApiResponse<Void>> deleteBoothLike(@PathVariable Long boothId,@AuthenticationPrincipal CustomUserDetails userDetails){

        boothService.deleteBoothLike(boothId, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
