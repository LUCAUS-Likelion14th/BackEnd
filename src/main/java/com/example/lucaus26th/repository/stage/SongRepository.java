package com.example.lucaus26th.repository.stage;

import com.example.lucaus26th.domain.stage.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long> {
    Optional<Song> findByIdAndStageId(Long songId, Long stageId);
}
