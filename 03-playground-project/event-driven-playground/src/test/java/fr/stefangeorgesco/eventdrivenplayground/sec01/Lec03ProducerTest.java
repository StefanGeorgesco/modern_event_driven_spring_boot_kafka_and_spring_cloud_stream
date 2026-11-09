package fr.stefangeorgesco.eventdrivenplayground.sec01;

import fr.stefangeorgesco.eventdrivenplayground.sec19.ProducerRunner;
import fr.stefangeorgesco.eventdrivenplayground.sec19.dto.Order;
import fr.stefangeorgesco.eventdrivenplayground.sec19.dto.ProductType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.KafkaHeaders;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
        classes = ProducerRunner.class,
        properties = {
                "section=sec19",
                "config=04-producer"
        }
)
class Lec03ProducerTest extends AbstractTest {

    @Test
    void orderProducer() {
        var message1 = this.receive(ORDER_EVENTS, Order.class);
        var message2 = this.receive(ORDER_EVENTS, Order.class);

        // we can not use receivedKey. We use test binder, and we test how the message is produced
        assertEquals(1, message1.getHeaders().get(KafkaHeaders.KEY, Integer.class));
        assertEquals(1, message1.getPayload().id());
        assertEquals(ProductType.DIGITAL, message1.getPayload().productType());

        assertEquals(2, message2.getHeaders().get(KafkaHeaders.KEY, Integer.class));
        assertEquals(2, message2.getPayload().id());
        assertEquals(ProductType.PHYSICAL, message2.getPayload().productType());
    }

}
