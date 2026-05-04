package com.example.lucaus26th.domain.stamp;

import com.example.lucaus26th.domain.booth.Booth;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StampBooth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booth_id") // 연관관계의 주인
    private Booth booth;

    @Builder
    public StampBooth(Booth booth){
        this.booth = booth;
    }

    public void update(Booth booth){
        this.booth = booth;
    }
}
