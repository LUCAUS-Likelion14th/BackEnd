package com.example.lucaus26th.dto.response.stamp;

import lombok.Builder;
import lombok.Getter;

public class StampResponseDto {
    @Getter
    @Builder
    public static class Stamp{
        private String name;
        private String student_id;
        private Long stamp_count;
        private Long stamp_all;
        //private List<>
    }

    @Getter
    @Builder
    public static class My{
        private Long stamp_count;
        private Long stamp_all;

        public static StampResponseDto.My from(Long stampCount, Long stampAll){
            return My.builder()
                    .stamp_count(stampCount)
                    .stamp_all(stampAll).build();
        }
    }
}
