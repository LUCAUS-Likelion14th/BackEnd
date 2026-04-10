package com.example.lucaus26th.dto.request.lost;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class LostRequestDto {

    private String category;
    private String name;
    @Schema(type = "string", format = "binary", description = "분실물 이미지 파일")
    private MultipartFile image;
    @Pattern(regexp = "^(0[1-9]|1[0-2])\\.(0[1-9]|[12][0-9]|3[01])$", message = "날짜 형식이 올바르지 않습니다. (MM.DD)")
    private String date;
    private String findLocation;
    private String storage;
}
