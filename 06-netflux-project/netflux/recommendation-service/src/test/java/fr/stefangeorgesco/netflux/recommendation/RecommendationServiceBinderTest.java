package fr.stefangeorgesco.netflux.recommendation;

import fr.stefangeorgesco.netflux.recommendation.repository.CustomerGenreRepository;
import fr.stefangeorgesco.netflux.recommendation.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.EnableTestBinder;
import org.springframework.cloud.stream.binder.test.InputDestination;

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
    }

    @Test
    void movieAddedEvent() {
    }

}
