package com.example.lucaus26th.global.exception;

import lombok.Getter;

// ErrorCode를 예외로 전달하기 위한 예외 컨테이너 객체
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    // 상세 메시지 생성자 (상세 정보를 덧붙이고 싶을 때 사용) 이거 추가!
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
