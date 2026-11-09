package fr.stefangeorgesco.eventdrivenplayground.sec02;

import fr.stefangeorgesco.eventdrivenplayground.sec19.ProducerRunner;
import fr.stefangeorgesco.eventdrivenplayground.sec19.dto.Order;
import fr.stefangeorgesco.eventdrivenplayground.sec19.dto.ProductType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
        classes = ProducerRunner.class,
        properties = {
                "section=sec19",
                "config=04-producer",
                "spring.cloud.function.definition=producer;orderConsumer",
                "spring.cloud.stream.bindings.orderConsumer-in-0.destination=order-events",
                "spring.cloud.stream.kafka.binder.consumer-properties.key.deserializer=org.apache.kafka.common.serialization.IntegerDeserializer",
                "spring.cloud.stream.kafka.binder.consumer-properties.auto.offset.reset=earliest"
        }
)
@Import(Lec03ProducerTest.TestConsumerConfiguration.class)
class Lec03ProducerTest extends AbstractTest {

    @Autowired
    private BlockingQueue<Message<Order>> orderQueue;

    @Test
    void orderProducer() throws InterruptedException {
        var message1 = this.orderQueue.poll(5, TimeUnit.SECONDS);
        var message2 = this.orderQueue.poll(5, TimeUnit.SECONDS);

        assertNotNull(Objects.requireNonNull(message1));
        assertEquals(1, message1.getHeaders().get(KafkaHeaders.RECEIVED_KEY, Integer.class));
        assertEquals(1, message1.getPayload().id());
        assertEquals(ProductType.DIGITAL, message1.getPayload().productType());

        assertNotNull(Objects.requireNonNull(message2));
        assertEquals(2, message2.getHeaders().get(KafkaHeaders.RECEIVED_KEY, Integer.class));
        assertEquals(2, message2.getPayload().id());
        assertEquals(ProductType.PHYSICAL, message2.getPayload().productType());
    }

    @Configuration
    static class TestConsumerConfiguration {

        @Bean
        public BlockingQueue<Message<Order>> orderQueue() {
            return new LinkedBlockingQueue<>(); // Thread-safe queue to hold received messages
        }

        @Bean
        public Consumer<Message<Order>> orderConsumer(BlockingQueue<Message<Order>> orderQueue) {
            return orderQueue::add;
        }
    }
}
