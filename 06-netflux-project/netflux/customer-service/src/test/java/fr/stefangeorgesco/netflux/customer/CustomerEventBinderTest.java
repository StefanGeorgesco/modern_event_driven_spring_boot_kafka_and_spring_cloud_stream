package fr.stefangeorgesco.netflux.customer;

import fr.stefangeorgesco.netflux.customer.dto.GenreUpdateRequest;
import fr.stefangeorgesco.netflux.netflux.events.CustomerGenreUpdatedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.EnableTestBinder;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.test.web.servlet.client.RestTestClient;
import tools.jackson.databind.json.JsonMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EnableTestBinder
@AutoConfigureRestTestClient
class CustomerEventBinderTest {

    @Autowired
    private RestTestClient testClient;

    @Autowired
    private OutputDestination outputDestination;

    @Test
    void genreUpdatedEvent() {
        // send the genre update request to the REST endpoint
        var request = new GenreUpdateRequest("Thriller");
        this.testClient.patch()
                .uri("/api/customers/2/genre")
                .body(request)
                .exchange()
                .expectStatus().isNoContent();

        // validate the event
        var message = this.outputDestination.receive(1000, "customer-events");
        var event = JsonMapper.shared().readValue(message.getPayload(), CustomerGenreUpdatedEvent.class);
        assertEquals(2, message.getHeaders().get(KafkaHeaders.KEY, Integer.class));
        assertEquals(2, event.customerId());
        assertEquals("Thriller", event.favoriteGenre());
    }
}
