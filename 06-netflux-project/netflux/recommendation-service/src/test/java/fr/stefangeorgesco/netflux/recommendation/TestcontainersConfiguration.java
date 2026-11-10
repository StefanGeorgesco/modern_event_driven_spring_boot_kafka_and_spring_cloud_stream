package fr.stefangeorgesco.netflux.recommendation;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    KafkaContainer kafkaContainer() {
        return new KafkaContainer(DockerImageName.parse("apache/kafka:4.4.0"));
    }

    // @ServiceConnection only exposes a KafkaConnectionDetails bean, which Spring Boot's
    // Kafka autoconfiguration uses, but the Spring Cloud Stream Kafka binder ignores it
    // and falls back to localhost:9092. So we also give the broker address to the binder.
    @Bean
    DynamicPropertyRegistrar kafkaBinderProperties(KafkaContainer kafkaContainer) {
        return registry -> registry.add("spring.cloud.stream.kafka.binder.brokers",
                kafkaContainer::getBootstrapServers);
    }
}
