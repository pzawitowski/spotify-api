package com.spotifys.controller;

import com.spotifys.dto.SpotifyIdRequest;
import com.spotifys.dto.TrackItem;
import com.spotifys.dto.Tracks;
import com.spotifys.entity.FavouriteTrack;
import com.spotifys.service.SpotifyService;
import com.spotifys.service.TracksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class SearchController {

    @Autowired
    SpotifyService spotifyService;

    @Autowired
    TracksService tracksService;


    @GetMapping("/find")
    public Tracks search(@RequestParam(name = "q") String query, @RequestParam String type) {
        return spotifyService.findTracks(query, type);
    }

    @PostMapping("/add")
    public ResponseEntity addToFavourites(@RequestBody SpotifyIdRequest request) {
        TrackItem item = spotifyService.getTrack(request.getId());

        FavouriteTrack favouriteTrack = new FavouriteTrack();
        favouriteTrack.setSpotifyId(item.getId());
        favouriteTrack.setName(item.getName());

        String createdId = tracksService.addFavouriteTrack(favouriteTrack);

        return ResponseEntity.created(URI.create(String.format("/favourites/%s", createdId))).build();
    }


}
