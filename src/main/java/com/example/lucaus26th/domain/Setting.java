package com.example.lucaus26th.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // form : "10:00 - 17:00"
    private String mon;
    private String tue;
    private String wed;
    private String thu;
    private String fri;

    // 혹시 one to many 같은 거 추가해야 하면
    // 여기 아래부터 booth 관련 코드 추가하기

    // 여기 아래부터 food 관련 코드 추가하기


    // 생성자
    @Builder
    public Setting(String mon, String tue, String wed, String thu, String fri) {
        // 모든 요일 검증
        validateTimeFormat(mon);
        validateTimeFormat(tue);
        validateTimeFormat(wed);
        validateTimeFormat(thu);
        validateTimeFormat(fri);
        
        // 포맷 검증 통과 시 필드 채움
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
    }
    private void validateTimeFormat(String time){
        if (time == null || !time.matches("\\d{2}:\\d{2} - \\d{2}:\\d{2}")){
            throw new IllegalArgumentException("형식 오류 (HH:MM - HH:MM): " + time);
        }
    }
}
