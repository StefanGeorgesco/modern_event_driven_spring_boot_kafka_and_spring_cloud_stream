package fr.stefangeorgesco.eventdrivenplayground.sec17;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;


@EnableScheduling
@SpringBootApplication(scanBasePackages = "fr.stefangeorgesco.eventdrivenplayground.${section}.consumer")
public class ConsumerRunner {

    static void main() {
        SpringApplication.run(
                ConsumerRunner.class,
                "--section=sec17",
                "--config=01-consumer"
        );
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder().build();
    }
}
