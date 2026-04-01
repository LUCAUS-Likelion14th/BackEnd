package com.example.lucaus26th.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StageCategory {
    STUDENT_PERFORMANCE("학생 공연"), //학생 공연
    CHEONGRYONG_FESTIVAL("청룡 가요제"), //청룡 가요제
    ARTIST_PERFORMANCE("아티스트 공연"), //아티스트 공연
    STAGE_EXHIBITION("무대 기획전"); //무대 기획전

    private final String displayName;
}
