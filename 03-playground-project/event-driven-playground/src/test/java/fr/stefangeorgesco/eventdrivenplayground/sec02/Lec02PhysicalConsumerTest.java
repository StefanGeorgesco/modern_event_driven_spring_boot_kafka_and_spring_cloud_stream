package fr.stefangeorgesco.eventdrivenplayground.sec02;

import fr.stefangeorgesco.eventdrivenplayground.sec19.PhysicalDeliveryConsumerRunner;
import fr.stefangeorgesco.eventdrivenplayground.sec19.dto.PhysicalDelivery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.messaging.support.MessageBuilder;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest(
        classes = PhysicalDeliveryConsumerRunner.class,
        properties = {
                "section=sec19",
                "config=02-physical-consumer"
        }
)
class Lec02PhysicalConsumerTest extends AbstractTest {

    @Test
    void physicalConsumer(CapturedOutput output) {
        var physicalDelivery = new PhysicalDelivery(1, "123 non main street", "Atlanta");
        var message = MessageBuilder.withPayload(physicalDelivery).build();
        this.streamBridge.send(PHYSICAL_DELIVERY, message);
        Awaitility.await()
                .atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> assertTrue(output.getOut().contains("received: " + physicalDelivery)));
    }

}
