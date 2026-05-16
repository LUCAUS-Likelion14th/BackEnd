package com.example.lucaus26th.controller.logs;

import com.example.lucaus26th.domain.logs.EventLog;
import com.example.lucaus26th.dto.request.logs.EventRequestDto;
import com.example.lucaus26th.repository.logs.EventLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventLogRepository eventLogRepository;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<Void> saveEvent(
            @RequestBody EventRequestDto request
    ) throws JsonProcessingException {

        EventLog eventLog = EventLog.builder()
                .eventType(request.getEvent_type())
                .userId(request.getUser_id())
                .sessionId(request.getSession_id())
                .timestamp(request.getTimestamp())
                .payload(
                        objectMapper.writeValueAsString(
                                request.getPayload()
                        )
                )
                .build();

        eventLogRepository.save(eventLog);

        return ResponseEntity.ok().build();
    }
}