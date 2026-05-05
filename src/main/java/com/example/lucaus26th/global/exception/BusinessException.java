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
}
