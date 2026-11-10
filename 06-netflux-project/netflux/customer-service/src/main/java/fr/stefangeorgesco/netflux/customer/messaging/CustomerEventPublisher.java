package fr.stefangeorgesco.netflux.customer.messaging;

import fr.stefangeorgesco.netflux.netflux.events.CustomerGenreUpdatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CustomerEventPublisher {

    @EventListener
    public void onGenreUpdatedEvent(CustomerGenreUpdatedEvent genreUpdatedEvent) {
        // TO DO
    }
}
