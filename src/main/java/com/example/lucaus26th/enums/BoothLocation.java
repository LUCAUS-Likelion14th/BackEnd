package com.example.lucaus26th.enums;

import lombok.Getter;

@Getter
public enum BoothLocation {
    SQUARE("서라벌홀 일대"),
    BACKGATE("후문 일대"),
    FIELD("대운동장");

    private final String description;

    BoothLocation(String description){
        this.description = description;
    }

}
