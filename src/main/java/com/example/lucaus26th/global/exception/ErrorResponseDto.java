package com.example.lucaus26th.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {
    private String code;
    private String message;

    // 커스텀 에러코드 + 메시지 반환
    public static ErrorResponseDto of(ErrorCode errorCode) {
        return new ErrorResponseDto(errorCode.getCode(), errorCode.getMessage());
    }
}

/*
### 동작 흐름
```
validateTimeFormat() → BusinessException(ErrorCode.INVALID_TIME_FORMAT) 발생 [BusinessException]
        ↓
GlobalExceptionHandler가 BusinessException을 전역으로 잡음 [GlobalExceptionHandler]
        ↓
ErrorCode에서 HttpStatus, code, message를 꺼냄 [ErrorCode]
        ↓
400 Bad Request + {"code": "TIME_400_1", "message": "형식 오류 (HH:MM - HH:MM)입니다."} [ErrorResponseDto]
*/