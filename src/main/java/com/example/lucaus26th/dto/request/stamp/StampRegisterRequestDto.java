package com.example.lucaus26th.dto.request.stamp;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StampRegisterRequestDto {
    @NotBlank
    private String name;
    @Size(min = 8, max = 8)
    @JsonProperty("student_id")
    private String studentId;
}
