package com.spotifys.dto;

public class SpotifyIdRequest {
    private String id;
    public SpotifyIdRequest() {

    }
    public SpotifyIdRequest(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
