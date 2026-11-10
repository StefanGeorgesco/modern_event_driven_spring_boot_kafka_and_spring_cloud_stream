package fr.stefangeorgesco.netflux.movie;

import fr.stefangeorgesco.netflux.movie.dto.MovieDetails;
import fr.stefangeorgesco.netflux.netflux.events.MovieAddedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.test.web.servlet.client.RestTestClient;
import tools.jackson.databind.json.JsonMapper;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureRestTestClient
@Import({
        TestcontainersConfiguration.class,
        MovieEventKafkaTest.TestConsumerConfiguration.class
})
@SuppressWarnings("SpringBootApplicationProperties")
@SpringBootTest(
        properties = {
                "app.import-movies=false",
                "spring.cloud.function.definition=testConsumer",
                "spring.cloud.stream.bindings.testConsumer-in-0.destination=movie-events",
                "spring.cloud.stream.kafka.binder.consumer-properties.key.deserializer=org.apache.kafka.common.serialization.IntegerDeserializer",
                "spring.cloud.stream.kafka.binder.consumer-properties.auto.offset.reset=earliest"
        }
)
class MovieEventKafkaTest {

    @Autowired
    private RestTestClient testClient;

    @Autowired
    private BlockingQueue<Message<MovieAddedEvent>> queue;

    @Test
    void movieAddedEvent() throws InterruptedException {
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
        var message = Objects.requireNonNull(this.queue.poll(5, TimeUnit.SECONDS));
        var event = message.getPayload();
        assertEquals(responseBody.id(), message.getHeaders().get(KafkaHeaders.RECEIVED_KEY, Integer.class));
        assertEquals(responseBody.id(), event.movieId());
        assertEquals("The Matrix", event.title());
    }

    @TestConfiguration
    static class TestConsumerConfiguration {

        @Bean
        public BlockingQueue<Message<MovieAddedEvent>> queue() {
            return new LinkedBlockingQueue<>();
        }

        @Bean
        Consumer<Message<MovieAddedEvent>> testConsumer(BlockingQueue<Message<MovieAddedEvent>> queue) {
            return queue::add;
        }
    }
}
