package com.example.lucaus26th.controller.lost;

import com.example.lucaus26th.dto.response.lost.LostResponseDto;
import com.example.lucaus26th.global.api.ApiResponse;
import com.example.lucaus26th.service.lost.LostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lost")
@RequiredArgsConstructor
@Tag(name = "분실물", description = "분실물 관련 API")
public class LostController {

    private final LostService lostService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<LostResponseDto>>> getLost(@RequestParam(required = false) String category,
                                                                      @RequestParam(required = false) String date,
                                                                      @RequestParam(defaultValue = "0") int page){
        Pageable pageable = PageRequest.of(page, 6);
        Page<LostResponseDto> response = lostService.getLost(category, date, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }


}
