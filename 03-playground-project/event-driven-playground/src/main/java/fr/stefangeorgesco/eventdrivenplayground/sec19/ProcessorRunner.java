package fr.stefangeorgesco.eventdrivenplayground.sec19;

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
                "--section=sec19",
                "--config=03-processor"
        );
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder().build();
    }
}
