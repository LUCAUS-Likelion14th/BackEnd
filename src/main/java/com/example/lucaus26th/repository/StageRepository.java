package com.example.lucaus26th.repository;

import com.example.lucaus26th.domain.Stage;
import com.example.lucaus26th.domain.StageCategory;
import com.example.lucaus26th.dto.response.StageResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StageRepository extends JpaRepository<Stage, Long> {

    List<Stage> findByDateAndCategoryOrderByStartAtAsc(LocalDate date, StageCategory category);

    StageCategory category(StageCategory category);

    List<Stage> findByDateOrderByStartAtAsc(LocalDate date);
}
