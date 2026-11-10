package fr.stefangeorgesco.netflux.movie;

import fr.stefangeorgesco.netflux.movie.dto.MovieDetails;
import fr.stefangeorgesco.netflux.netflux.events.MovieAddedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.EnableTestBinder;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.test.web.servlet.client.RestTestClient;
import tools.jackson.databind.json.JsonMapper;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;


@EnableTestBinder
@AutoConfigureRestTestClient
@SuppressWarnings("SpringBootApplicationProperties")
@SpringBootTest(properties = "app.import-movies=false")
class MovieEventBinderTest {

    @Autowired
    private RestTestClient testClient;

    @Autowired
    private OutputDestination outputDestination;

    @Test
    void movieAddedEvent() {
        // create a new movie using the REST endpoint
        var json = """
                {
                    "title": "The Matrix",
                    "rating": 0.5,
                    "votes": 250,
                    "releaseDate": "1999-03-31",
                    "budget": 1000000,
                    "runtime": 120,
                    "backdropPath": "/backdrop.jpg",
                    "revenue": 500000,
                    "homepage": "https://www.thematrix.com",
                    "overview": "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.",
                    "popularity": 0.8,
                    "posterPath": "/poster.jpg",
                    "genres": ["Action", "Science Fiction"]
                }
                """;
        var movieDetails = JsonMapper.shared().readValue(json, MovieDetails.class);
        var responseBody = Objects.requireNonNull(this.testClient.post()
                .uri("/api/movies")
                .body(movieDetails)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(MovieDetails.class)
                .returnResult()
                .getResponseBody());

        // validate the event
        var message = this.outputDestination.receive(1000, "movie-events");
        var event = JsonMapper.shared().readValue(message.getPayload(), MovieAddedEvent.class);
        assertEquals(responseBody.id(), message.getHeaders().get(KafkaHeaders.KEY, Integer.class));
        assertEquals(responseBody.id(), event.movieId());
        assertEquals("The Matrix", event.title());
    }
}
