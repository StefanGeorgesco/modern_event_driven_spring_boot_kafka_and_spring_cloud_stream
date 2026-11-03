package fr.stefangeorgesco.eventdrivenplayground.sec12.producer;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class Producer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Producer.class);
    private static final String DEMO_OUT = "demo-out";
    private final StreamBridge streamBridge;

    public Producer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public void run(String @NonNull ... args) throws Exception {
        for (int i = 1; i <= 1_000_000; i++) {
            this.streamBridge.send(DEMO_OUT, "msg-" + i);
            if (i % 1_000 == 0) {
                log.info("Total messages produced: {}", i);
            }
        }
    }
}
