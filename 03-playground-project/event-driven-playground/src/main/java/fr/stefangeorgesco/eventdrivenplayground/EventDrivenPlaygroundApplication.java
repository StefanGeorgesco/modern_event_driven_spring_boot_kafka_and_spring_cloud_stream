package fr.stefangeorgesco.eventdrivenplayground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@SpringBootApplication
public class EventDrivenPlaygroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventDrivenPlaygroundApplication.class, args);
	}

	@Bean
	public ObjectMapper objectMapper() {
		return JsonMapper.builder().build();
	}

}
