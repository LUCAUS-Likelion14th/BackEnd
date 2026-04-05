package com.example.lucaus26th.dto.response.notice;

import com.example.lucaus26th.domain.notice.Notice;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ActiveNoticeResponseDto {
    private Long id;
    private String title;

    public static ActiveNoticeResponseDto from(Notice notice) {
        return ActiveNoticeResponseDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .build();
    }
}
