package com.example.lucaus26th.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.EnumSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum StageCategory {
    STUDENT_PERFORMANCE("학생 공연",
            EnumSet.of(StageEndpoint.LIVE, StageEndpoint.LINEUP, StageEndpoint.TIMETABLE)),
    CHEONGRYONG_FESTIVAL("청룡 가요제",
            EnumSet.of(StageEndpoint.LIVE, StageEndpoint.LINEUP, StageEndpoint.TIMETABLE)),
    ARTIST_PERFORMANCE("아티스트 공연",
            EnumSet.of(StageEndpoint.LIVE, StageEndpoint.LINEUP, StageEndpoint.TIMETABLE)),
    STAGE_EXHIBITION("무대 기획전",
            EnumSet.of(StageEndpoint.TIMETABLE)),
    EVENT("행사",
            EnumSet.of(StageEndpoint.TIMETABLE));

    private final String displayName;
    private final Set<StageEndpoint> defaultEndpoints;
}
