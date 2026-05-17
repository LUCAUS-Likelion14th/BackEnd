package com.example.lucaus26th.controller.logs;

import com.example.lucaus26th.domain.logs.EventLog;
import com.example.lucaus26th.dto.request.logs.EventRequestDto;
import com.example.lucaus26th.repository.logs.EventLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventLogRepository eventLogRepository;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<Void> saveEvent(
            @RequestBody EventRequestDto request
    ) {

        try {

            EventLog eventLog = EventLog.builder()
                    .eventType(request.getEventType())
                    .userId(request.getUserId())
                    .sessionId(request.getSessionId())
                    .targetType(request.getTargetType())
                    .targetId(request.getTargetId())
                    .payload(
                            objectMapper.writeValueAsString(
                                    Optional.ofNullable(request.getPayload())
                                            .orElse(Collections.emptyMap())
                            )
                    )
                    .build();

            eventLogRepository.save(eventLog);

        } catch (Exception e) {
            log.error("이벤트 저장 실패", e);
        }

        return ResponseEntity.ok().build();
    }
}