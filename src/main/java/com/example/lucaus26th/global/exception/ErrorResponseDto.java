package com.example.lucaus26th.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {
    private String message;
}

/*### 동작 흐름
```
validateTimeFormat() → IllegalArgumentException 발생
        ↓
GlobalExceptionHandler가 전역으로 낚아챔
        ↓
400 Bad Request + {"message": "형식 오류 (HH:MM - HH:MM): ..."}*/