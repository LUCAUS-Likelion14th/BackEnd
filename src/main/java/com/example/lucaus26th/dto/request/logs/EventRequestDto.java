package com.example.lucaus26th.dto.request.logs;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class EventRequestDto {

    private String eventType;

    private Long userId;

    private String sessionId;

    private String targetType;

    private Long targetId;

    private Map<String, Object> payload;
}