package com.example.lucaus26th.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// BusinessException을 잡아서 그 안의 errorcode를 응답으로 바꾸어 줌
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 우리가 정의한 BusinessException을 처리
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponseDto.of(errorCode));
    }

    // 잘못된 요청값 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)  // 400
                .body(new ErrorResponseDto(
                        ErrorCode.INVALID_INPUT_VALUE.getCode(),
                        e.getMessage()
                ));
    }

    // 예상하지 못한 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception e) {
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ErrorResponseDto.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}
