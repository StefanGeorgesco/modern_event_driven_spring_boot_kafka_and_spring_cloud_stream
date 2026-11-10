package fr.stefangeorgesco.netflux.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;

@SpringBootTest
@AutoConfigureRestTestClient
class CustomerApiTest {

    @Autowired
    private RestTestClient testClient;

    @Test
    void customerDetails() {
        this.testClient.get()
                .uri("/api/customers/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Sam")
                .jsonPath("$.favoriteGenre").isEqualTo("Action");
    }

    @Test
    void customerNotFound() {
        this.testClient.get()
                .uri("/api/customers/999")
                .exchange()
                .expectStatus().isNotFound();
    }
}
