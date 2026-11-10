package fr.stefangeorgesco.netflux.recommendation;

import fr.stefangeorgesco.netflux.recommendation.repository.CustomerGenreRepository;
import fr.stefangeorgesco.netflux.recommendation.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Import;

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
    }

    @Test
    void movieAddedEvent() {
    }
}
