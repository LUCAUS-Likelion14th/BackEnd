package com.example.lucaus26th.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 공연 가시성 범위.
 * - DEFAULT: 모든 엔드포인트에 표시 (일반 공연)
 * - TIMETABLE_ONLY: 타임테이블·라이브에만 표시, 라인업/상세에서는 제외
 *   (예: EVENT, 청룡가요제 예선/본선, 일부 특수 학생무대)
 * - LINEUP_ONLY: 라인업·상세에만 표시, 타임테이블/라이브에서는 제외
 *   (예: 청룡가요제 참가팀)
 */
@Getter
@RequiredArgsConstructor
public enum StageVisibility {
    DEFAULT,
    TIMETABLE_ONLY,
    LINEUP_ONLY
}
