package fr.stefangeorgesco.eventdrivenplayground.sec16;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@SpringBootApplication
public class SectionRunner {

    static void main() {
        SpringApplication.run(SectionRunner.class,
                "--section=sec16",
                "--config=04-dlq");
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder().build();
    }
}
