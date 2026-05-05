package com.example.lucaus26th.repository.notice;

import com.example.lucaus26th.domain.notice.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findAllByOrderByImportantDescCreatedAtDesc(Pageable pageable);
    Optional<Notice> findFirstByActiveTrueOrderByCreatedAtDesc();
    Optional<Notice> findByActiveTrue();
}
