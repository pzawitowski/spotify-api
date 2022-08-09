package com.spotifys;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotifys.dto.TrackItem;
import com.spotifys.dto.SpotifySearchResponse;
import com.spotifys.dto.Tracks;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpotifyFindTest {
    //    TestRestTemplate restTemplate = new TestRestTemplate();

    @LocalServerPort
    int port;

    @Autowired
    ObjectMapper objectMapper;

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

    @BeforeEach
    void initialize() {

    }



    @Test
    void testSearchOneResponse() throws JsonProcessingException, InterruptedException {
        // given
        TrackItem item = thereIsOneTrackOfNameTestInSpotifyCatalog();
        // when
        Tracks tracks = searchingForTrackWithNameTest();
        // then
        shouldReturnOneTrackWithNameTest(item, tracks);
    }

    private void shouldReturnOneTrackWithNameTest(TrackItem item, Tracks tracks) {
        assertNotNull(tracks, "Tracks should be not null");
        assertNotNull(tracks.getItems(), "Track items should be not null");
        assertEquals(1, tracks.getItems().size());
        assertIterableEquals(Arrays.asList(item), tracks.getItems());
    }

    @Nullable
    private Tracks searchingForTrackWithNameTest() {

                return WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/find")
                        .queryParam("q", "Test")
                        .queryParam("type", "track")
                        .build()
                )
                .exchange()

                .expectStatus().isOk().expectBody(Tracks.class).returnResult().getResponseBody();
    }

    @NotNull
    private TrackItem thereIsOneTrackOfNameTestInSpotifyCatalog() throws JsonProcessingException {
        TrackItem item = new TrackItem();
        item.setId("5678");
        item.setName("Test track");

        Tracks tracks = new Tracks(Arrays.asList(item));

        SpotifySearchResponse searchResponse = new SpotifySearchResponse(tracks);

        mockSpotifyApi.enqueue(
                new MockResponse()
                        .setBody(objectMapper.writeValueAsString(searchResponse))
                        .addHeader("Content-Type", "application/json"));
        return item;
    }
}
