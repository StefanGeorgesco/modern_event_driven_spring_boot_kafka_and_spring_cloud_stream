package fr.stefangeorgesco.eventdrivenplayground.sec10;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;


@EnableScheduling
@SpringBootApplication(scanBasePackages = "fr.stefangeorgesco.eventdrivenplayground.${section}.processor")
public class ProcessorRunner {

    static void main() {
        SpringApplication.run(
                ProcessorRunner.class,
                "--section=sec10",
                "--config=04-processor"
        );
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder().build();
    }
}
