package com.example.lucaus26th.dto.response;

import com.example.lucaus26th.domain.Song;
import com.example.lucaus26th.domain.Stage;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonPropertyOrder({ "song_id", "title", "play_order" })
public class SongResponseDTO {
    @JsonProperty("song_id")
    private Long songId;

    private String title;

    @JsonProperty("play_order")
    private Integer playOrder;

    public static SongResponseDTO from(Song song) {
        return SongResponseDTO.builder()
                .songId(song.getId())
                .title(song.getTitle())
                .playOrder(song.getPlayOrder())
                .build();
    }
}
