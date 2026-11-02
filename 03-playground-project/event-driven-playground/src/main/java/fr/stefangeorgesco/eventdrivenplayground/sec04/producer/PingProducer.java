package fr.stefangeorgesco.eventdrivenplayground.sec04.producer;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
public class PingProducer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(PingProducer.class);
    private static final String PING_OUT = "ping-out";
    private final StreamBridge streamBridge;

    public PingProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public void run(String @NonNull ... args) throws Exception {
        // linux/mac: "ping", "-c", "10", "google.com"
        // windows  : "ping", "-n", "10", "google.com"
        var process = new ProcessBuilder("ping", "-n", "10", "google.com")
                .redirectErrorStream(true)
                .start();
        // linux/Mac: UTF-8
        // windows  : OEM code page of the console (IBM850 for fr-FR), not Cp1252
        try (var reader = process.inputReader(Charset.forName("IBM850"))) {
            reader.lines()
                  .forEach(line -> {
                      log.info("sending: {}", line);
                      this.streamBridge.send(PING_OUT, line);
                  });
        }
    }
}