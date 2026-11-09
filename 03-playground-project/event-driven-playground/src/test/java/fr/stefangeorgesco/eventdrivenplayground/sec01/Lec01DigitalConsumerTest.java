package fr.stefangeorgesco.eventdrivenplayground.sec01;

import fr.stefangeorgesco.eventdrivenplayground.sec19.DigitalDeliveryConsumerRunner;
import fr.stefangeorgesco.eventdrivenplayground.sec19.dto.DigitalDelivery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.messaging.support.MessageBuilder;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest(
        classes = DigitalDeliveryConsumerRunner.class,
        properties = {
                "section=sec19",
                "config=01-digital-consumer"
        }
)
class Lec01DigitalConsumerTest extends AbstractTest {

    @Test
    void digitalConsumer(CapturedOutput output) {
        var digitalDelivery = new DigitalDelivery(1, "sam@gmail.com");
        var message = MessageBuilder.withPayload(digitalDelivery).build();
        this.inputDestination.send(message, DIGITAL_DELIVERY);
        assertTrue(output.getOut().contains("received: " + digitalDelivery));
    }
}
