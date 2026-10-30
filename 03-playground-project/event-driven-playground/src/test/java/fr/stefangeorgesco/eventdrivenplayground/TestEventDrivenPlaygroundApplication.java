package fr.stefangeorgesco.eventdrivenplayground;

import org.springframework.boot.SpringApplication;

public class TestEventDrivenPlaygroundApplication {

	static void main(String[] args) {
		SpringApplication.from(EventDrivenPlaygroundApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
