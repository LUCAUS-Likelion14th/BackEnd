package com.example.lucaus26th.controller.notice;

import com.example.lucaus26th.dto.request.notice.NoticeRequestDto;
import com.example.lucaus26th.dto.response.notice.NoticeResponseDto;
import com.example.lucaus26th.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // 공지 생성
    @PostMapping("/admin/notice")
    public ResponseEntity<NoticeResponseDto> createNotice(@RequestBody NoticeRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(noticeService.createNotice(request));
    }

    // 공지 전체 조회
    @GetMapping("/notice")
    public ResponseEntity<List<NoticeResponseDto>> getNotices() {
        return ResponseEntity.ok(noticeService.getALLNotice());
    }

    // 공지 수정
    @PatchMapping("/admin/notice/{noticeId}")
    public ResponseEntity<NoticeResponseDto> patchNotice(@PathVariable Long noticeId, @RequestBody NoticeRequestDto request) {
        return ResponseEntity.ok(noticeService.patchNotice(noticeId, request));
    }

    // 공지 삭제
    @DeleteMapping("/admin/notice/{noticeId}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.noContent().build();
    }

    // 상세 조회
    @GetMapping("notice/{noticeId}")
    public ResponseEntity<NoticeResponseDto> getNotice(@PathVariable Long noticeId) {
        return ResponseEntity.ok(noticeService.getNotice(noticeId));
    }

    // 중요 반전
    @PatchMapping("/admin/notice/{noticeId}/important")
    public ResponseEntity<NoticeResponseDto> toggleImportant(@PathVariable Long noticeId) {
        return ResponseEntity.ok(noticeService.toggleImportant(noticeId));
    }

    // 실시간 반전
    @PatchMapping("/admin/notice/{noticeId}/active")
    public ResponseEntity<NoticeResponseDto> toggleActive(@PathVariable Long noticeId) {
        return ResponseEntity.ok(noticeService.toggleActive(noticeId));
    }
}