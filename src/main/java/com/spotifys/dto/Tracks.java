package com.spotifys.dto;

import java.util.List;

public class Tracks {
    private List<TrackItem> items;

    public Tracks() {

    }

    public Tracks(List<TrackItem> items) {
        this.items = items;
    }

    public List<TrackItem> getItems() {
        return items;
    }

    public void setItems(List<TrackItem> items) {
        this.items = items;
    }
}
