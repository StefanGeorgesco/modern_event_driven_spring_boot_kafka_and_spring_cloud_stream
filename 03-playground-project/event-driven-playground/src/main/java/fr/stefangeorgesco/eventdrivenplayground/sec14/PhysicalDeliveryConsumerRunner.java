package fr.stefangeorgesco.eventdrivenplayground.sec14;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;


@SpringBootApplication(scanBasePackages = "fr.stefangeorgesco.eventdrivenplayground.${section}.consumer")
public class PhysicalDeliveryConsumerRunner {

    static void main() {
        SpringApplication.run(
                PhysicalDeliveryConsumerRunner.class,
                "--section=sec14",
                "--config=02-physical-consumer"
        );
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder().build();
    }
}
