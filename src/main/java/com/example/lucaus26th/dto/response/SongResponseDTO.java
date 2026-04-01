package com.example.lucaus26th.dto.response;

import com.example.lucaus26th.domain.Song;
import com.example.lucaus26th.domain.Stage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SongResponseDTO {
    @JsonProperty("song_id")
    private Long songId;

    @JsonProperty("song_title")
    private String songTitle;

    public static SongResponseDTO from(Song song) {
        return SongResponseDTO.builder()
                .songId(song.getId())
                .songTitle(song.getTitle())
                .build();
    }
}
