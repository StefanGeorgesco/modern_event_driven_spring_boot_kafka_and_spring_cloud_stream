package fr.stefangeorgesco.eventdrivenplayground.sec08;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;


@SpringBootApplication(scanBasePackages = "fr.stefangeorgesco.eventdrivenplayground.${section}.producer")
public class ProducerRunner {

    static void main() {
        SpringApplication.run(
                ProducerRunner.class,
                "--section=sec08",
                "--config=02-producer"
        );
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder().build();
    }
}
