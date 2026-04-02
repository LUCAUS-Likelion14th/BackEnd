package com.example.lucaus26th.repository.stage;

import com.example.lucaus26th.domain.stage.Stage;
import com.example.lucaus26th.domain.stage.StageCategory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface StageRepository extends JpaRepository<Stage, Long> {

    List<Stage> findByDateAndCategoryOrderByStartAtAsc(LocalDate date, StageCategory category);

    StageCategory category(StageCategory category);

    List<Stage> findByDateOrderByStartAtAsc(LocalDate date);

    Optional<Stage> findByDateAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
            LocalDate date,
            LocalTime now1,
            LocalTime now2
    );

    @EntityGraph(attributePaths = {"stageInfo", "songs"})
    Optional<Stage> findById(Long id);
}
