package com.spotifys;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotifys.dto.SpotifyIdRequest;
import com.spotifys.dto.TrackItem;
import com.spotifys.entity.FavouriteTrack;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.net.InetAddress;

import static org.springframework.boot.test.context.SpringBootTest.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SpotifyAddFavouriteTest {
    @LocalServerPort
    private int port;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MongoTemplate mongoTemplate;

    public static MockWebServer mockSpotifyApi;

    @BeforeAll
    static void setup() throws IOException {
        mockSpotifyApi = new MockWebServer();
        mockSpotifyApi.start(InetAddress.getByName("localhost"), 9099);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockSpotifyApi.shutdown();
    }

    @Test
    void addTrackToFavourites() throws JsonProcessingException {
        String trackId = "99999999";
        // given
        TrackItem item = thereIsTrackInSpotifyCatalogWithId(trackId);
        // when
        WebTestClient.ResponseSpec response = addingTrackWithId(trackId);
        // then API response is ok
        response.expectStatus().isCreated();
        // and
        thereShouldBeAnEntryInDBCreatedWithTrackId(trackId);
    }

    @NotNull
    private TrackItem thereIsTrackInSpotifyCatalogWithId(String id) throws JsonProcessingException {
        TrackItem item = new TrackItem();
        item.setId(id);
        item.setName("Test track");

        mockSpotifyApi.enqueue(
                new MockResponse()
                        .setBody(objectMapper.writeValueAsString(item))
                        .addHeader("Content-Type", "application/json"));
        return item;
    }

    public WebTestClient.ResponseSpec addingTrackWithId(String trackId) {
             return WebTestClient.bindToServer()
                    .baseUrl(String.format("http://localhost:%s", port))
                    .build()
                    .post()

                    .uri(uriBuilder -> uriBuilder
                            .path("/add")
                            .build()
                    ) .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(new SpotifyIdRequest(trackId))
                    .exchange();
    }


    private void thereShouldBeAnEntryInDBCreatedWithTrackId(String trackId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("spotifyId").is(trackId));
        FavouriteTrack favouriteTrack = mongoTemplate.findOne(query, FavouriteTrack.class);
        Assertions.assertNotNull(favouriteTrack);
    }

}
