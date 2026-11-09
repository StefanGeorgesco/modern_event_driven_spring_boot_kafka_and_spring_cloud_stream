package fr.stefangeorgesco.eventdrivenplayground.sec02;

import fr.stefangeorgesco.eventdrivenplayground.sec19.DigitalDeliveryConsumerRunner;
import fr.stefangeorgesco.eventdrivenplayground.sec19.dto.DigitalDelivery;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.messaging.support.MessageBuilder;

import java.time.Duration;

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
        this.streamBridge.send(DIGITAL_DELIVERY, message);
        Awaitility.await()
                .atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> assertTrue(output.getOut().contains("received: " + digitalDelivery)));
    }
}
