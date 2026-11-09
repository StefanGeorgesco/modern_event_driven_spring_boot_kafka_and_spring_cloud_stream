package fr.stefangeorgesco.eventdrivenplayground.sec02;

import fr.stefangeorgesco.eventdrivenplayground.sec19.ProcessorRunner;
import fr.stefangeorgesco.eventdrivenplayground.sec19.dto.DigitalDelivery;
import fr.stefangeorgesco.eventdrivenplayground.sec19.dto.Order;
import fr.stefangeorgesco.eventdrivenplayground.sec19.dto.PhysicalDelivery;
import fr.stefangeorgesco.eventdrivenplayground.sec19.dto.ProductType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(
        classes = ProcessorRunner.class,
        properties = {
                "section=sec19",
                "config=03-processor",
                "spring.cloud.function.definition=deliveryProcessor;digitalDeliveryConsumer;physicalDeliveryConsumer",
                "spring.cloud.stream.bindings.digitalDeliveryConsumer-in-0.destination=digital-delivery",
                "spring.cloud.stream.bindings.physicalDeliveryConsumer-in-0.destination=physical-delivery",
                "spring.cloud.stream.kafka.binder.consumer-properties.auto.offset.reset=earliest"
        }
)
@Import(Lec04ProcessorTest.TestConsumerConfiguration.class)
public class Lec04ProcessorTest extends AbstractTest {

    @Autowired
    private BlockingQueue<Message<DigitalDelivery>> digitalDeliveryQueue;

    @Autowired
    private BlockingQueue<Message<PhysicalDelivery>> physicalDeliveryQueue;

    @Test
    void digitalOrder() throws InterruptedException {
        // produce a digital order via order-events topic
        var order = new Order(1, 1, ThreadLocalRandom.current().nextInt(1, 1000),
                ProductType.DIGITAL);
        var inputMessage = MessageBuilder.withPayload(order).build();
        this.streamBridge.send(ORDER_EVENTS, inputMessage);

        // we should expect a digital delivery message on digital-delivery topic
        var outputMessage = this.digitalDeliveryQueue.poll(5, TimeUnit.SECONDS);
        assertNotNull(Objects.requireNonNull(outputMessage));
        assertEquals(1, outputMessage.getPayload().orderId());
        assertNotNull(outputMessage.getPayload().email());

        // there should not be any message on physical-delivery topic
        assertNull(this.physicalDeliveryQueue.poll(1000, TimeUnit.MILLISECONDS));
    }

    @Test
    void physicalOrder() throws InterruptedException {
        // produce a physical order via order-events topic
        var order = new Order(2, 1, ThreadLocalRandom.current().nextInt(1, 1000),
                ProductType.PHYSICAL);
        var inputMessage = MessageBuilder.withPayload(order).build();
        this.streamBridge.send(ORDER_EVENTS, inputMessage);

        // we should expect a physical delivery message on physical-delivery topic
        var outputMessage = this.physicalDeliveryQueue.poll(5, TimeUnit.SECONDS);
        assertNotNull(Objects.requireNonNull(outputMessage));
        assertEquals(2, outputMessage.getPayload().orderId());
        assertNotNull(outputMessage.getPayload().street());

        // there should not be any message on digital-delivery topic
        assertNull(this.digitalDeliveryQueue.poll(1000, TimeUnit.MILLISECONDS));
    }

    @Configuration
    static class TestConsumerConfiguration {

        @Bean
        public BlockingQueue<Message<DigitalDelivery>> digitalDeliveryQueue() {
            return new LinkedBlockingQueue<>(); // Thread-safe queue to hold received messages
        }

        @Bean
        public BlockingQueue<Message<PhysicalDelivery>> physicalDeliveryQueue() {
            return new LinkedBlockingQueue<>(); // Thread-safe queue to hold received messages
        }

        @Bean
        public Consumer<Message<DigitalDelivery>> digitalDeliveryConsumer(BlockingQueue<Message<DigitalDelivery>> digitalDeliveryQueue) {
            return digitalDeliveryQueue::add;
        }

        @Bean
        public Consumer<Message<PhysicalDelivery>> physicalDeliveryConsumer(BlockingQueue<Message<PhysicalDelivery>> physicalDeliveryQueue) {
            return physicalDeliveryQueue::add;
        }
    }
}
