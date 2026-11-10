package fr.stefangeorgesco.netflux.movie;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.client.RestTestClient;

@AutoConfigureRestTestClient
@SuppressWarnings("SpringBootApplicationProperties")
@SpringBootTest(properties = "app.import-movies=false")
@Sql(value = "classpath:test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class MovieApiTest {

    @Autowired
    private RestTestClient testClient;

    @Test
    void movieDetails() {
        this.testClient.get()
                .uri("/api/movies/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.title").isEqualTo("Inception")
                .jsonPath("$.runtime").isEqualTo(150);
    }

    @Test
    void movieNotFound() {
        this.testClient.get()
                .uri("/api/movies/999")
                .exchange()
                .expectStatus().isNotFound();
    }
}
