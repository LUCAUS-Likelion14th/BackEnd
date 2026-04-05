package com.example.lucaus26th.dto.request.lost;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class LostRequestDto {

    private String category;
    private String name;
    private String image;
    @Pattern(regexp = "^(0[1-9]|1[0-2])\\.(0[1-9]|[12][0-9]|3[01])$", message = "날짜 형식이 올바르지 않습니다. (MM.DD)")
    private String date;
    @JsonProperty("find_location")
    private String findLocation;
    private String storage;
}
