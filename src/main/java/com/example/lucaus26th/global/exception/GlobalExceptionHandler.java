package com.example.lucaus26th.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.servlet.multipart.max-file-size:}")
    private String maxFileSize;

    // 우리가 정의한 BusinessException을 처리
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponseDto.of(errorCode.getCode(), e.getMessage()));
    }

    // multipart 파일 용량 초과 처리 (max-file-size / max-request-size 초과 시 발생)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponseDto> handleMaxUploadSize(MaxUploadSizeExceededException e) {
        log.warn("파일 용량 초과: {}", e.getMessage());
        ErrorCode errorCode = ErrorCode.FILE_TOO_LARGE;
        String message = maxFileSize == null || maxFileSize.isBlank()
                ? errorCode.getMessage()
                : "파일 용량이 너무 큽니다. 최대 " + maxFileSize + "까지 업로드 가능합니다.";
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponseDto.of(errorCode.getCode(), message));
    }

    // 잘못된 요청값 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("잘못된 요청값", e);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.of(
                        ErrorCode.INVALID_INPUT_VALUE.getCode(),
                        e.getMessage()
                ));
    }
    
    // 봇 노이즈 404 처리 -> 500 으로 알림 안오도록
    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    public ResponseEntity<Void> handleNoResourceFound(org.springframework.web.servlet.resource.NoResourceFoundException e) {
        return ResponseEntity.notFound().build();
    }

    // 예상하지 못한 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception e) {
        log.error("예상하지 못한 서버 예외 발생", e);

        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ErrorResponseDto.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}
