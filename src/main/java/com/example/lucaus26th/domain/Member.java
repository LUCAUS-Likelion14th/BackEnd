package com.example.lucaus26th.domain;

import com.example.lucaus26th.enums.MemberRole;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    private Integer studentID;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    @Builder
    public Member(String email) {
        this.email = email;
        this.role = MemberRole.USER; // USER를 기본값으로 설정
    }

    public void updateMember(String name, Integer studentID) {
        this.name = name;
        this.studentID = studentID;
    }

    public void updateRole(MemberRole role) {
        this.role = role;
    }
}
