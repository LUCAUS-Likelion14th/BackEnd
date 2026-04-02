package com.example.lucaus26th.domain.booth;

import com.example.lucaus26th.dto.request.booth.BoothSettingRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoothSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booth_id")
    private Booth booth;
    @Column(nullable = false)
    private LocalDate date;
    @Column(name = "day_of_week",nullable = false)
    private String day;
    @Column(nullable = false)
    private LocalTime startAt;
    @Column(nullable = false)
    private LocalTime endAt;

    public void update(BoothSettingRequestDto request, Booth booth){
        if (booth != null){
            this.booth = booth;
        }
        if(request.getDate() != null){
            this.date = request.getDate();
        }
        if(request.getDay() != null){
            this.day = request.getDay();
        }
        if(request.getStartAt() != null){
            this.startAt = request.getStartAt();
        }
        if(request.getEndAt() != null){
            this.endAt = request.getEndAt();
        }
    }

}