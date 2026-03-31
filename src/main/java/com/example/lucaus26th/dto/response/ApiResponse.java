package com.example.lucaus26th.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private T data;

    public static <T> ApiResponse<T> of (T data){
        return new ApiResponse<>(data);
    }
}

/* Controller에서 밑처럼 쓰면 됨.
* return ResponseEntity.ok(ApiResponse.of(response));
* */
