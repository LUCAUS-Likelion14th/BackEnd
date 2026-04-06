package com.example.lucaus26th.enums;

import lombok.Getter;

@Getter
public enum BoothLocation {
    SQUARE("해방광장"),
    BACKGATE("후문"),
    FIELD("운동장");

    private final String description;

    BoothLocation(String description){
        this.description = description;
    }

}
