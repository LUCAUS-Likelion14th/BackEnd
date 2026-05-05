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

    private String studentID;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    private boolean isApplied = false;

    @Builder
    public Member(String email) {
        this.email = email;
        this.role = MemberRole.USER; // USER를 기본값으로 설정
        this.isApplied = false;
    }

    public void updateMemberInfo(String name, String studentId) {
        this.name = name;
        this.studentID = studentId;
    }

    public void updateRole(MemberRole role) {
        this.role = role;
    }

    public void apply() {
        this.isApplied = true;
    }
}
