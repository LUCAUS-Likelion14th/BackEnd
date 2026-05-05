package com.example.lucaus26th.dto.response.stamp;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class StampResponseDto {
    @Getter
    @Builder
    public static class Stamp{
        private String name;
        private String student_id;
        private Long stamp_count;
        private Long stamp_all;
        private List<BoothInfo> booths; // 리스트 추가

        public static StampResponseDto.Stamp from(String name, String studentId, Long stampCount, Long stampAll, List<BoothInfo> booths){
            return Stamp.builder()
                    .name(name)
                    .student_id(studentId)
                    .stamp_count(stampCount)
                    .stamp_all(stampAll)
                    .booths(booths)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class BoothInfo {
        private Long booth_id;
        private String name;
        private Boolean is_stamped;
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
