package com.spotifys.dto;

import com.spotifys.dto.Tracks;

public class SpotifySearchResponse {
    private Tracks tracks;

    public SpotifySearchResponse() {

    }

    public SpotifySearchResponse(Tracks tracks) {
        this.tracks = tracks;
    }

    public Tracks getTracks() {
        return tracks;
    }

    public void setTracks(Tracks tracks) {
        this.tracks = tracks;
    }
}
