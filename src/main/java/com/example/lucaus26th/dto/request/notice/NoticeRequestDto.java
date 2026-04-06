package com.example.lucaus26th.dto.request.notice;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeRequestDto {
    private String title;
    private String content;
    private Boolean important;
    private Boolean active;
}
