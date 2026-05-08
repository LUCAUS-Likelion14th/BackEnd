package com.example.lucaus26th.service.notice;

import com.example.lucaus26th.domain.notice.Notice;
import com.example.lucaus26th.dto.request.notice.NoticeRequestDto;
import com.example.lucaus26th.dto.response.notice.ActiveNoticeResponseDto;
import com.example.lucaus26th.dto.response.notice.NoticeResponseDto;
import com.example.lucaus26th.global.exception.BusinessException;
import com.example.lucaus26th.global.exception.ErrorCode;
import com.example.lucaus26th.repository.notice.NoticeRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public NoticeResponseDto createNotice(NoticeRequestDto request) {
        Notice notice = new Notice(
                request.getTitle(),
                request.getContent(),
                request.getImportant() != null ? request.getImportant() : false,
                false
        );

        Notice savedNotice = noticeRepository.save(notice);
        return NoticeResponseDto.from(savedNotice);
    }

    @Transactional(readOnly = true)
    public Page<NoticeResponseDto> getALlNotice(Pageable pageable) {
        return noticeRepository.findAllByOrderByImportantDescCreatedAtDesc(pageable)
                .map(NoticeResponseDto::from);
    }

    public NoticeResponseDto patchNotice(Long noticeId, NoticeRequestDto request) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));

        notice.update(
                request.getTitle() != null ? request.getTitle() : notice.getTitle(),
                request.getContent() != null ? request.getContent() : notice.getContent(),
                request.getImportant() != null ? request.getImportant() : notice.isImportant(),
                false
        );

        return NoticeResponseDto.from(notice);
    }

    public void deleteNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));

        noticeRepository.delete(notice);
    }

    @Transactional(readOnly = true)
    public NoticeResponseDto getNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));

        return NoticeResponseDto.from(notice);
    }

    public NoticeResponseDto toggleImportant(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));

        notice.toggleImportant();

        return NoticeResponseDto.from(notice);
    }

    public NoticeResponseDto activateNotice(Long noticeId) {
        Notice target = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));

        // 기존 active 공지 있으면 해제
        noticeRepository.findByActiveTrue()
                .ifPresent(Notice::deactivate);

        // 선택 공지 활성화
        target.activate();

        return NoticeResponseDto.from(target);
    }

    public NoticeResponseDto deactivateNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));

        notice.deactivate();

        return NoticeResponseDto.from(notice);
    }

    @Transactional(readOnly = true)
    public ActiveNoticeResponseDto getActiveNotice() {
        Notice notice = noticeRepository
                .findFirstByActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new BusinessException(ErrorCode.ACTIVE_NOTICE_NOT_FOUND));

        return ActiveNoticeResponseDto.from(notice);
    }
}
