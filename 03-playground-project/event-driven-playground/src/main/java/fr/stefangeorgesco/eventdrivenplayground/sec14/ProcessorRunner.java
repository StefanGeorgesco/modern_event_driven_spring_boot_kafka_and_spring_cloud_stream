package fr.stefangeorgesco.eventdrivenplayground.sec14;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;


@SpringBootApplication(scanBasePackages = "fr.stefangeorgesco.eventdrivenplayground.${section}.processor")
public class ProcessorRunner {

    static void main() {
        SpringApplication.run(
                ProcessorRunner.class,
                "--section=sec14",
                "--config=03-processor",
                "--processing.mode=ordered"
        );
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder().build();
    }
}
