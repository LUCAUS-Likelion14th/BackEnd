package com.example.lucaus26th.repository.notice;

import com.example.lucaus26th.domain.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findAllByOrderByImportantDescCreatedAtDesc();
}
