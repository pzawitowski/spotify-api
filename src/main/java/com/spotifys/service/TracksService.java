package com.spotifys.service;

import com.spotifys.entity.FavouriteTrack;
import com.spotifys.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TracksService {
    @Autowired
    TrackRepository favouriteTrackRepository;

    public String addFavouriteTrack(FavouriteTrack favouriteTrack) {
        return favouriteTrackRepository.save(favouriteTrack).getId();
    }
}
