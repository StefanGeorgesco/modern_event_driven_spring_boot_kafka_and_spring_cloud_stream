package fr.stefangeorgesco.netflux.recommendation;

import fr.stefangeorgesco.netflux.netflux.events.CustomerGenreUpdatedEvent;
import fr.stefangeorgesco.netflux.netflux.events.MovieAddedEvent;
import fr.stefangeorgesco.netflux.recommendation.repository.CustomerGenreRepository;
import fr.stefangeorgesco.netflux.recommendation.repository.MovieRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Import;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import org.testcontainers.shaded.org.awaitility.core.ThrowingRunnable;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class RecommendationServiceKafkaTest {

    @Autowired
    private StreamBridge streamBridge;

    @Autowired
    private CustomerGenreRepository customerGenreRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void customerGenreUpdatedEvent() {
        var event = new CustomerGenreUpdatedEvent(1, "Action", Instant.now());
        streamBridge.send("customer-events", event);
        waitUntilAsserted(() -> {
            var customerGenre = this.customerGenreRepository.findById(1).orElseThrow(Assertions::fail);
            assertEquals("Action", customerGenre.getFavoriteGenre());
        });
    }

    @Test
    void movieAddedEvent() {
        var event = new MovieAddedEvent(
                1,
                "The Matrix",
                150,
                110,
                "2023-01-01",
                List.of("Action"),
                "https://image.tmdb.org/t/p/w500/poster.jpg",
                Instant.now()
        );
        streamBridge.send("movie-events", event);
        waitUntilAsserted(() -> {
            var movie = this.movieRepository.findById(1).orElseThrow(Assertions::fail);
            assertEquals("The Matrix", movie.getTitle());
        });
    }

    private void waitUntilAsserted(ThrowingRunnable assertion) {
        Awaitility.await()
                .pollInterval(Duration.ofMillis(500))
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(assertion);
    }
}
