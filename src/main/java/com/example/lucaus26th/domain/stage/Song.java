package com.example.lucaus26th.domain.stage;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "song")
@Getter
@NoArgsConstructor
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stage_id", nullable = false)
    private Stage stage;

    @Column(nullable = false)
    private Integer playOrder;

    @Column(nullable = false)
    private String title;

    private Song(String title, Integer playOrder) {
        this.title = title;
        this.playOrder = playOrder;
    }

    public void assignStage(Stage stage) {
        this.stage = stage;
    }

    public static Song create(String title, Integer playOrder) {
        return new Song(title, playOrder);
    }

    public void updateSong(String title, Integer playOrder) {
        this.title = title;
        this.playOrder = playOrder;
    }

}
