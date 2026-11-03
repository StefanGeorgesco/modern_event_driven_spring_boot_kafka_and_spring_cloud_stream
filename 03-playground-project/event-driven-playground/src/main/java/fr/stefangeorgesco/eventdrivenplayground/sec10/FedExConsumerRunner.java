package fr.stefangeorgesco.eventdrivenplayground.sec10;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;


@SpringBootApplication(scanBasePackages = "fr.stefangeorgesco.eventdrivenplayground.${section}.consumer")
public class FedExConsumerRunner {

    static void main() {
        SpringApplication.run(
                FedExConsumerRunner.class,
                "--section=sec10",
                "--config=02-fedex-consumer"
        );
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder().build();
    }
}
