package fr.stefangeorgesco.netflux.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;

@SpringBootTest
@AutoConfigureRestTestClient
class CustomerApiTest {

    @Autowired
    private RestTestClient testClient;

    @Test
    void customerDetails() {
    }

    @Test
    void customerNotFound() {
    }
}
