package fr.stefangeorgesco.eventdrivenplayground.sec01;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(properties = {
		"section=sec00",
		"config=tests"
})
@SuppressWarnings("SpringBootApplicationProperties")
class SectionRunnerTests {

	@Test
	void contextLoads() {
	}
}
