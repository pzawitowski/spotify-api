package com.spotifys.entity;

import org.springframework.data.annotation.Id;

public class FavouriteTrack {
    @Id
    private String id;
    private String spotifyId;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public java.lang.String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(java.lang.String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
