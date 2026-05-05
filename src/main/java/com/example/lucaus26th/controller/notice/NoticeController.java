package com.example.lucaus26th.controller.notice;

import com.example.lucaus26th.dto.request.notice.NoticeRequestDto;
import com.example.lucaus26th.dto.response.notice.NoticeResponseDto;
import com.example.lucaus26th.global.api.ApiResponse;
import com.example.lucaus26th.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // 공지 생성
    @PostMapping("/admin/notice")
    public ResponseEntity<ApiResponse<NoticeResponseDto>> createNotice(@RequestBody NoticeRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(noticeService.createNotice(request)));
    }

    // 공지 전체 조회
    @GetMapping("/notice")
    public ResponseEntity<ApiResponse<Page<NoticeResponseDto>>> getAllNotice(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(noticeService.getALlNotice(pageable)));
    }

    // 공지 수정
    @PatchMapping("/admin/notice/{noticeId}")
    public ResponseEntity<ApiResponse<NoticeResponseDto>> patchNotice(
            @PathVariable Long noticeId,
            @RequestBody NoticeRequestDto request
    ) {
        return ResponseEntity.ok(ApiResponse.success(noticeService.patchNotice(noticeId, request)));
    }

    // 공지 삭제
    @DeleteMapping("/admin/notice/{noticeId}")
    public ResponseEntity<ApiResponse<Void>> deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 상세 조회
    @GetMapping("/notice/{noticeId}")
    public ResponseEntity<ApiResponse<NoticeResponseDto>> getNotice(@PathVariable Long noticeId) {
        return ResponseEntity.ok(ApiResponse.success(noticeService.getNotice(noticeId)));
    }

    // 중요 반전
    @PatchMapping("/admin/notice/{noticeId}/important")
    public ResponseEntity<ApiResponse<NoticeResponseDto>> toggleImportant(@PathVariable Long noticeId) {
        return ResponseEntity.ok(ApiResponse.success(noticeService.toggleImportant(noticeId)));
    }

    // 메인 공지 설정
    @PatchMapping("/admin/notice/{noticeId}/active")
    public ResponseEntity<ApiResponse<NoticeResponseDto>> activateNotice(
            @PathVariable Long noticeId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(noticeService.activateNotice(noticeId))
        );
    }

    // 메인 공지 해제
    @PatchMapping("/admin/notice/{noticeId}/inactive")
    public ResponseEntity<ApiResponse<NoticeResponseDto>> deactivateNotice(
            @PathVariable Long noticeId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(noticeService.deactivateNotice(noticeId))
        );
    }
}