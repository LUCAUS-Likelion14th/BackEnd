package com.example.lucaus26th.repository;

import com.example.lucaus26th.domain.Stage;
import com.example.lucaus26th.domain.StageCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StageRepository extends JpaRepository<Stage, Long> {

    List<Stage> findByDateAndCategoryOrderByStartAtAsc(LocalDate date, StageCategory category);

    StageCategory category(StageCategory category);
}
