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

    // promotion
    PROMOTION_NOT_FOUND(HttpStatus.NOT_FOUND, "PROMOTION_4041", "해당 프로모션을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;


}
