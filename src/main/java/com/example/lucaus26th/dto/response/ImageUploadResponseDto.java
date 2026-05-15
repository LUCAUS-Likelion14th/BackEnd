package com.example.lucaus26th.dto.response;

import lombok.AllArgsConstructor;

import lombok.Getter;

@Getter

@AllArgsConstructor

public class ImageUploadResponseDto {

    private String originalName;
    private String key;
    private String url;

}
