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
    }

    @Test
    void movieNotFound() {
    }
}
