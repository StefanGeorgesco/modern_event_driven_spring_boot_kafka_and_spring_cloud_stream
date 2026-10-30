package fr.stefangeorgesco.eventdrivenplayground.sec02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@SpringBootApplication
public class SectionRunner {

    @SuppressWarnings("unused")
    static void main(String[] args) {
        SpringApplication.run(
                SectionRunner.class,
                "--section=sec02",
                "--config=01-reactive-consumer"
        );
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder().build();
    }
}
