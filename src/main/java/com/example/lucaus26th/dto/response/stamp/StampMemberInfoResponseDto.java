package com.example.lucaus26th.dto.response.stamp;

import com.example.lucaus26th.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class StampMemberInfoResponseDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info{
        private String name;
        private String student_id;

        public static StampMemberInfoResponseDto.Info from(Member member){
            return Info.builder()
                    .name(member.getName())
                    .student_id(member.getStudentID())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Check{
        private Boolean is_registered;

        public static StampMemberInfoResponseDto.Check from(Boolean check){
            return Check.builder()
                    .is_registered(check).build();
        }
    }

}
