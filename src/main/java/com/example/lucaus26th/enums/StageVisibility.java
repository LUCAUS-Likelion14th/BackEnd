package com.example.lucaus26th.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.EnumSet;
import java.util.Set;

/**
 * 공연 가시성 override.
 * - DEFAULT: 카테고리 기본 노출(StageCategory.defaultEndpoints) 사용
 * - LINEUP_ONLY: 라인업에만 노출 (예: 청룡가요제 참가팀)
 * - LIVE_AND_TIMETABLE: 라이브/타임테이블에만 노출 (예: 학생 공연 일부)
 * - TIMETABLE_ONLY: 타임테이블에만 노출
 *
 * endpoints가 null이면 카테고리 기본 노출을 사용한다.
 */
@Getter
@RequiredArgsConstructor
public enum StageVisibility {
    DEFAULT(null),
    LINEUP_ONLY(EnumSet.of(StageEndpoint.LINEUP)),
    LIVE_AND_TIMETABLE(EnumSet.of(StageEndpoint.LIVE, StageEndpoint.TIMETABLE)),
    TIMETABLE_ONLY(EnumSet.of(StageEndpoint.TIMETABLE));

    private final Set<StageEndpoint> endpoints;
}
