package com.example.lucaus26th.repository.logs;

import com.example.lucaus26th.domain.logs.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventLogRepository extends JpaRepository<EventLog, Long> {
}
