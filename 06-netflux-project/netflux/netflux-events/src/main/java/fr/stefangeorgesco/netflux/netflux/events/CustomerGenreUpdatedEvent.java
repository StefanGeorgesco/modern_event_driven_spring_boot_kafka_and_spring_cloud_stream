package fr.stefangeorgesco.netflux.netflux.events;

import java.time.Instant;

public record CustomerGenreUpdatedEvent(Integer customerId,
                                        String favoriteGenre,
                                        Instant updatedAt) {

}
