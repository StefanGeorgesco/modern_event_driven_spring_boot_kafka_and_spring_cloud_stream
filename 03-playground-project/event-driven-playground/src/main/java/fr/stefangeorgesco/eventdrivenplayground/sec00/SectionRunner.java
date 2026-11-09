package fr.stefangeorgesco.eventdrivenplayground.sec00;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SectionRunner {

    static void main() {
        SpringApplication.run(
                SectionRunner.class,
                "--section=sec00",
                "--config=tests"
        );
    }
}
