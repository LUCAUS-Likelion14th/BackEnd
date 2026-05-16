package com.example.lucaus26th.dto.request.logs;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class EventRequestDto {

    private String event_type;
    private Long user_id;
    private String session_id;
    private LocalDateTime timestamp;
    private Map<String, Object> payload;
}