package com.example.lucaus26th.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 공연 가시성 override.
 * - DEFAULT: 카테고리 기본 노출(StageCategory.defaultEndpoints)을 그대로 따름
 * - TIMETABLE_ONLY: 라이브/라인업에서 추가로 숨김. 타임테이블에만 표시
 *   (예: 청룡가요제 예선/본선 블록, 특수 학생 무대)
 */
@Getter
@RequiredArgsConstructor
public enum StageVisibility {
    DEFAULT,
    TIMETABLE_ONLY
}
