package com.example.lucaus26th.domain.notice;

import com.example.lucaus26th.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Notice extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;
    private boolean important;
    private boolean active;

    public Notice(String title, String content, boolean important, boolean active) {
        this.title = title;
        this.content = content;
        this.important = important;
        this.active = active;
    }

    public void update(String title, String content, boolean important, boolean active) {
        this.title = title;
        this.content = content;
        this.important = important;
        this.active = active;
    }

    public void toggleImportant() {
        this.important = !this.important;
    }

    public void toggleActive() {
        this.active = !this.active;
    }
}
