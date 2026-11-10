package fr.stefangeorgesco.netflux.recommendation;

import fr.stefangeorgesco.netflux.netflux.events.CustomerGenreUpdatedEvent;
import fr.stefangeorgesco.netflux.recommendation.dto.MovieRecommendations;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.cloud.stream.binder.test.EnableTestBinder;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.RestTestClient;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import static fr.stefangeorgesco.netflux.recommendation.dto.RecommendationType.NEWLY_ADDED;
import static fr.stefangeorgesco.netflux.recommendation.dto.RecommendationType.PERSONALIZED;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "server.shutdown=immediate") // do not wait for the open SSE connection on shutdown
@EnableTestBinder
@AutoConfigureRestTestClient
@AutoConfigureWebTestClient
@Sql(value = "classpath:test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class RecommendationServiceApiTest {

    @Autowired
    private RestTestClient restTestClient;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private InputDestination inputDestination;

    @Test
    void recommendations() {
        var recommandations = Objects.requireNonNull(this.restTestClient.get()
                .uri("/api/recommendations/1")
                .exchange()
                .expectStatus().isOk()
                .returnResult(new ParameterizedTypeReference<List<MovieRecommendations>>() {
                })
                .getResponseBody());
        assertEquals(2, recommandations.size());
        assertEquals(NEWLY_ADDED, recommandations.getFirst().type());
        assertEquals(9, recommandations.getFirst().movies().size());
        assertEquals(PERSONALIZED, recommandations.getLast().type());
        assertEquals(4, recommandations.getLast().movies().size());
    }

    @Test
    void recommendationsStreamGenreUpdated() {
        var event = new CustomerGenreUpdatedEvent(2, "Sci-Fi", Instant.now());
        inputDestination.send(MessageBuilder.withPayload(event).build(), "customer-events");

        var stream = this.webTestClient.get()
                .uri("/api/recommendations/2/stream")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
                .returnResult(MovieRecommendations.class)
                .getResponseBody();

        StepVerifier.create(stream.take(1))
                .assertNext(recommendation -> {
                    assertEquals(PERSONALIZED, recommendation.type());
                    assertEquals(2, recommendation.customerId());
                    assertEquals(4, recommendation.movies().size());
                })
                .verifyComplete();
    }
}
