package com.example.lucaus26th.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 공통
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_500", "서버 내부 오류가 발생했습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON_400", "잘못된 요청입니다."),

    // s3
    EMPTY_FILE(HttpStatus.BAD_REQUEST, "S3_4001", "빈 파일은 업로드할 수 없습니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3_5001", "파일 업로드에 실패했습니다."),

    // member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_4041", "해당 회원을 찾을 수 없습니다"),

    // booth
    BOOTH_NOT_FOUND(HttpStatus.NOT_FOUND,"BOOTH_4041", "해당 부스를 찾을 수 없습니다."),

    // 날짜 형식 (쿼리)
    WRONG_DATE_FORMAT(HttpStatus.BAD_REQUEST, "DATE_4001", "날짜 형식이 올바르지 않습니다. MMDD 형식으로 입력해주세요."),

    // stage
    STAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "STAGE_4041", "해당 공연을 찾을 수 없습니다."),
    STAGE_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "STAGE_INFO_4041", "해당 공연 상세 정보를 찾을 수 없습니다."),
    LIVE_STAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "STAGE_4041", "현재 진행 중인 공연이 없습니다."),
    SONG_NOT_FOUND(HttpStatus.NOT_FOUND, "SONG_4041", "해당 곡을 찾을 수 없습니다."),
    INVALID_STAGE_TIME(HttpStatus.BAD_REQUEST, "STAGE_4001", "공연 시작 시간은 종료 시간보다 빨라야 합니다."),

    // stamp
    NO_MEMBER_INFO(HttpStatus.BAD_REQUEST, "STAMP_4001", "학생 정보(이름&학번)를 입력하지 않았습니다."),
    UNSTAMPABLE_BOOTH(HttpStatus.BAD_REQUEST, "STAMP_4002", "도장을 찍을 수 없는 부스입니다."),
    NO_STAMP_PASSWORD(HttpStatus.CONFLICT, "STAMP_4091", "부스에 도장 비밀번호가 등록되어 있지 않습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "STAMP_4003", "비밀번호가 일치하지 않습니다."),
    ALREADY_STAMPED(HttpStatus.CONFLICT, "STAMP_4092", "이미 찍은 도장입니다."),
    ALREADY_APPLIED(HttpStatus.CONFLICT, "APPLY_4091", "이미 응모하셨습니다."),
    NOT_ENOUGH_STAMPS(HttpStatus.BAD_REQUEST,"APPLY_4001", "모든 스탬프를 모아야 응모할 수 있습니다."),

    // promotion
    PROMOTION_NOT_FOUND(HttpStatus.NOT_FOUND, "PROMOTION_4041", "해당 프로모션을 찾을 수 없습니다."),

    // lost
    WRONG_LOST_CATEGORY(HttpStatus.BAD_REQUEST, "LOST_4001", "카테고리는 '전자기기, 지갑/카드, 화장품, 우산, 기타' 에 속해야 합니다."),
    LOST_NOT_FOUND(HttpStatus.NOT_FOUND, "LOST_4041", "존재하지 않는 분실물입니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;


}
