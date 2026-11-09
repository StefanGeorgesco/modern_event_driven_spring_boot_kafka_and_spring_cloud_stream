package fr.stefangeorgesco.eventdrivenplayground.sec00;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(properties = {
		"section=sec00",
		"config=tests"
})
class SectionRunnerTests {

	@Test
	void contextLoads() {
	}
}
