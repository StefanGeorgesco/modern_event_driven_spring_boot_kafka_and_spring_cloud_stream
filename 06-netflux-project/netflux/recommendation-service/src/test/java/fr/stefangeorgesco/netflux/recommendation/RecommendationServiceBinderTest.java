package fr.stefangeorgesco.netflux.recommendation;

import fr.stefangeorgesco.netflux.netflux.events.CustomerGenreUpdatedEvent;
import fr.stefangeorgesco.netflux.netflux.events.MovieAddedEvent;
import fr.stefangeorgesco.netflux.recommendation.repository.CustomerGenreRepository;
import fr.stefangeorgesco.netflux.recommendation.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.EnableTestBinder;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.integration.support.MessageBuilder;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EnableTestBinder
class RecommendationServiceBinderTest {

    @Autowired
    private InputDestination inputDestination;

    @Autowired
    private CustomerGenreRepository customerGenreRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void customerGenreUpdatedEvent() {
        var event = new CustomerGenreUpdatedEvent(1, "Action", Instant.now());
        inputDestination.send(MessageBuilder.withPayload(event).build(), "customer-events");
        var customerGenre = this.customerGenreRepository.findById(1).orElseThrow();
        assertEquals("Action", customerGenre.getFavoriteGenre());
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
        inputDestination.send(MessageBuilder.withPayload(event).build(), "movie-events");
        var movie = this.movieRepository.findById(1).orElseThrow();
        assertEquals("The Matrix", movie.getTitle());
    }
}
