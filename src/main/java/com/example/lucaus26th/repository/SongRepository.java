package com.example.lucaus26th.repository;

import com.example.lucaus26th.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long> {
    Optional<Song> findByIdAndStageId(Long songId, Long stageId);
}
