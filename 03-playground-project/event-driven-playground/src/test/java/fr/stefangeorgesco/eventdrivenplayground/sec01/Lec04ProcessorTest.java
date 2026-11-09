package fr.stefangeorgesco.eventdrivenplayground.sec01;

import fr.stefangeorgesco.eventdrivenplayground.sec19.ProcessorRunner;
import fr.stefangeorgesco.eventdrivenplayground.sec19.dto.DigitalDelivery;
import fr.stefangeorgesco.eventdrivenplayground.sec19.dto.Order;
import fr.stefangeorgesco.eventdrivenplayground.sec19.dto.PhysicalDelivery;
import fr.stefangeorgesco.eventdrivenplayground.sec19.dto.ProductType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.support.MessageBuilder;

import java.util.concurrent.ThreadLocalRandom;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(
        classes = ProcessorRunner.class,
        properties = {
                "section=sec19",
                "config=03-processor"
        }
)
public class Lec04ProcessorTest extends AbstractTest {

    @Test
    void digitalOrder() {
        // produce a digital order via order-events topic
        var order = new Order(1, 1, ThreadLocalRandom.current().nextInt(1, 1000),
                ProductType.DIGITAL);
        var inputMessage = MessageBuilder.withPayload(order).build();
        this.inputDestination.send(inputMessage, ORDER_EVENTS);

        // we should expect a digital delivery message on digital-delivery topic
        var outputMessage = this.receive(DIGITAL_DELIVERY, DigitalDelivery.class);
        assertEquals(1, outputMessage.getPayload().orderId());
        assertNotNull(outputMessage.getPayload().email());

        // there should not be any message on physical-delivery topic
        assertNull(this.outputDestination.receive(1000, PHYSICAL_DELIVERY));
    }

    @Test
    void physicalOrder() {
        // produce a physical order via order-events topic
        var order = new Order(2, 1, ThreadLocalRandom.current().nextInt(1, 1000),
                ProductType.PHYSICAL);
        var inputMessage = MessageBuilder.withPayload(order).build();
        this.inputDestination.send(inputMessage, ORDER_EVENTS);

        // we should expect a physical delivery message on physical-delivery topic
        var outputMessage = this.receive(PHYSICAL_DELIVERY, PhysicalDelivery.class);
        assertEquals(2, outputMessage.getPayload().orderId());
        assertNotNull(outputMessage.getPayload().street());

        // there should not be any message on digital-delivery topic
        assertNull(this.outputDestination.receive(1000, DIGITAL_DELIVERY));
    }
}
