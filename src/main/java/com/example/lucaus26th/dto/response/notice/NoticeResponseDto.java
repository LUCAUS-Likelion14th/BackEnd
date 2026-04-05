package com.example.lucaus26th.dto.response.notice;

import com.example.lucaus26th.domain.notice.Notice;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoticeResponseDto {

    private Long id;
    private String title;
    private String content;
    private Boolean important;
    private Boolean active;

    public static NoticeResponseDto from(Notice notice) {
        return NoticeResponseDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .important(notice.isImportant())
                .active(notice.isActive())
                .build();
    }
}
