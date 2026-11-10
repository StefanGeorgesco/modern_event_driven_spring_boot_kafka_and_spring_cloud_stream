package fr.stefangeorgesco.netflux.recommendation.service;

import fr.stefangeorgesco.netflux.netflux.events.MovieAddedEvent;
import fr.stefangeorgesco.netflux.recommendation.dto.RecommendationEvents;
import fr.stefangeorgesco.netflux.recommendation.mapper.RecommendationMapper;
import fr.stefangeorgesco.netflux.recommendation.repository.MovieRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ApplicationEventPublisher eventPublisher;

    public MovieService(MovieRepository movieRepository, ApplicationEventPublisher eventPublisher) {
        this.movieRepository = movieRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void addMovie(MovieAddedEvent movieAddedEvent) {
        var entity = RecommendationMapper.toMovie(movieAddedEvent);
        movieRepository.save(entity);
        eventPublisher.publishEvent(new RecommendationEvents.NewMovieEvent(movieAddedEvent.movieId()));
    }
}
