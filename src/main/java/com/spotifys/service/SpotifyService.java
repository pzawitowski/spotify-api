package com.spotifys.service;

import com.spotifys.dto.SpotifySearchResponse;
import com.spotifys.dto.TrackItem;
import com.spotifys.dto.Tracks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class SpotifyService {

    @Autowired
    WebClient webClient;


    public Tracks findTracks(String query, String type) {
        Mono<SpotifySearchResponse> response = webClient.get().uri(
                        uriBuilder -> uriBuilder.
                                path("/search").
                                queryParam("q", query).
                                queryParam("type", type).
                                build()
                ).
                accept(MediaType.APPLICATION_JSON).
                retrieve().bodyToMono(SpotifySearchResponse.class);

        return response.block().getTracks();
    }

    public TrackItem getTrack(String trackId) {
        Mono<TrackItem> trackItemMono = webClient.get().
                uri("/tracks/{id}", trackId).accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(TrackItem.class));

        return trackItemMono.block();
    }

    public boolean isTrackIdValid(String trackId) {
       Mono<Boolean> response =
                        webClient.get().
                        uri("/tracks/{id}", trackId).
                        accept(MediaType.APPLICATION_JSON).
                        exchangeToMono(clientResponse -> Mono.just(clientResponse.statusCode().equals(HttpStatus.OK)));

        return response.block();
    }


}
