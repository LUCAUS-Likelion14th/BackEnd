package com.example.lucaus26th.domain.logs;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventType;

    private Long userId;

    private String sessionId;

    private String targetType;

    private Long targetId;

    @Column(columnDefinition = "json")
    private String payload;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}