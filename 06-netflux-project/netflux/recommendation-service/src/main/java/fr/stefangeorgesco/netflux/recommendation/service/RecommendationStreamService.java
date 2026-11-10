package fr.stefangeorgesco.netflux.recommendation.service;

import fr.stefangeorgesco.netflux.recommendation.dto.MovieRecommendations;
import fr.stefangeorgesco.netflux.recommendation.dto.RecommendationEvents;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Service
public class RecommendationStreamService { // RecommendationNotifier

    private final Sinks.Many<MovieRecommendations> recommandationSink
            = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE);
    private final Flux<MovieRecommendations> recommendationFlux = recommandationSink.asFlux();
    private final RecommendationService recommendationService;

    public RecommendationStreamService(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    public Flux<MovieRecommendations> streamRecommendations(Integer customerId) {
        return recommendationFlux
                .filter(recommendation ->
                        Objects.isNull(recommendation.customerId()) ||
                                recommendation.customerId().equals(customerId));
    }

    @Async
    @TransactionalEventListener
    public void onMovieAdded(RecommendationEvents.NewMovieEvent event) {
        var movie = recommendationService.findMovie(event.movieId());
        var recommandation = MovieRecommendations.newlyAdded(List.of(movie));
        recommandationSink.emitNext(recommandation,
                Sinks.EmitFailureHandler.busyLooping(Duration.ofSeconds(1)));
    }

    @Async
    @TransactionalEventListener
    public void onPersonalized(RecommendationEvents.PersonalizedEvent event) {
        var movies = recommendationService.findPersonalized(event.customerId());
        var recommandation = MovieRecommendations.personalized(event.customerId(), movies);
        recommandationSink.emitNext(recommandation,
                Sinks.EmitFailureHandler.busyLooping(Duration.ofSeconds(1)));
    }
}

