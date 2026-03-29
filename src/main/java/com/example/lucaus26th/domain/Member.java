package com.example.lucaus26th.domain;

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

    @Builder
    public Member(String email) {
        this.email = email;
    }

    public void updateMember(String name, Integer studentID) {
        this.name = name;
        this.studentID = studentID;
    }
}
