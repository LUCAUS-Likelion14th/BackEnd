package com.example.lucaus26th.dto.response.stamp;

import com.example.lucaus26th.domain.Member;
import lombok.Builder;
import lombok.Getter;

public class StampMemberInfoResponseDto {
    @Getter
    @Builder
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
    public static class Check{
        private Boolean is_registered;

        public static StampMemberInfoResponseDto.Check from(Boolean check){
            return Check.builder()
                    .is_registered(check).build();
        }
    }

}
