package com.example.lucaus26th.domain.booth;

import com.example.lucaus26th.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoothLike {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "booth_id")
    private Booth boothId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member memberId;
}
