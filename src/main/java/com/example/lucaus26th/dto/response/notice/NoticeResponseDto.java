package com.example.lucaus26th.dto.response.notice;

import com.example.lucaus26th.domain.notice.Notice;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NoticeResponseDto {

    private Long id;
    private String title;
    private String content;
    private Boolean important;
    private Boolean active;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    public static NoticeResponseDto from(Notice notice) {
        return NoticeResponseDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .important(notice.isImportant())
                .active(notice.isActive())
                .createdAt(notice.getCreatedAt())
                .build();
    }
}
