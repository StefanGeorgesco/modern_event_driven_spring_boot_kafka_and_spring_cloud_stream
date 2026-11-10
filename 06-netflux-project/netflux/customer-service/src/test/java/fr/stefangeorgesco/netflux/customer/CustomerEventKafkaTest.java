package fr.stefangeorgesco.netflux.customer;

import fr.stefangeorgesco.netflux.customer.dto.GenreUpdateRequest;
import fr.stefangeorgesco.netflux.netflux.events.CustomerGenreUpdatedEvent;
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

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureRestTestClient
@Import({
        TestcontainersConfiguration.class,
        CustomerEventKafkaTest.TestConsumerConfiguration.class
})
@SpringBootTest(
        properties = {
                "spring.cloud.function.definition=testConsumer",
                "spring.cloud.stream.bindings.testConsumer-in-0.destination=customer-events",
                "spring.cloud.stream.kafka.binder.consumer-properties.key.deserializer=org.apache.kafka.common.serialization.IntegerDeserializer",
                "spring.cloud.stream.kafka.binder.consumer-properties.auto.offset.reset=earliest"
        }
)
class CustomerEventKafkaTest {

    @Autowired
    private RestTestClient testClient;

    @Autowired
    private BlockingQueue<Message<CustomerGenreUpdatedEvent>> queue;

    @Test
    void genreUpdatedEvent() throws InterruptedException {
        // send the genre update request to the REST endpoint
        var request = new GenreUpdateRequest("Thriller");
        this.testClient.patch()
                .uri("/api/customers/2/genre")
                .body(request)
                .exchange()
                .expectStatus().isNoContent();

        // validate the event
        var message = Objects.requireNonNull(this.queue.poll(5, TimeUnit.SECONDS));
        var event = message.getPayload();
        assertEquals(2, message.getHeaders().get(KafkaHeaders.RECEIVED_KEY, Integer.class));
        assertEquals(2, event.customerId());
        assertEquals("Thriller", event.favoriteGenre());
    }

    @TestConfiguration
    static class TestConsumerConfiguration {

        @Bean
        public BlockingQueue<Message<CustomerGenreUpdatedEvent>> queue() {
            return new LinkedBlockingQueue<>();
        }

        @Bean
        Consumer<Message<CustomerGenreUpdatedEvent>> testConsumer(BlockingQueue<Message<CustomerGenreUpdatedEvent>> queue) {
            return queue::add;
        }
    }
}
