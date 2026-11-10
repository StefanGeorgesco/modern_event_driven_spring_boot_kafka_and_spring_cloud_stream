package fr.stefangeorgesco.netflux.recommendation.service;

import fr.stefangeorgesco.netflux.recommendation.dto.MovieRecommendations;
import fr.stefangeorgesco.netflux.recommendation.dto.RecommendationEvents;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class RecommendationStreamService { // RecommendationNotifier

    public Flux<MovieRecommendations> streamRecommendations(Integer customerId) {
        return Flux.empty();
    }

    @Async
    @EventListener // global
    public void onMovieAdded(RecommendationEvents.NewMovieEvent event) {
        // TO DO
    }

    @Async
    @EventListener
    public void onPersonalized(RecommendationEvents.PersonalizedEvent event) {
        // TO DO
    }
}

