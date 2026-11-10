package fr.stefangeorgesco.netflux.movie;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.EnableTestBinder;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.test.web.servlet.client.RestTestClient;


@EnableTestBinder
@AutoConfigureRestTestClient
@SuppressWarnings("SpringBootApplicationProperties")
@SpringBootTest(properties = "app.import-movies=false")
class MovieEventBinderTest {

    @Autowired
    private RestTestClient testClient;

    @Autowired
    private OutputDestination outputDestination;

    @Test
    void movieAddedEvent() {
    }
}
