package com.example.lucaus26th.domain.booth;

import com.example.lucaus26th.domain.BaseTimeEntity;
import com.example.lucaus26th.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoothLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "booth_id")
    private Booth booth;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
