package fr.stefangeorgesco.netflux.movie.service;

import fr.stefangeorgesco.netflux.movie.dto.MovieDetails;
import fr.stefangeorgesco.netflux.movie.exception.MovieNotFoundException;
import fr.stefangeorgesco.netflux.movie.mapper.MovieMapper;
import fr.stefangeorgesco.netflux.movie.repository.MovieRepository;
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

    public MovieDetails getMovie(Integer movieId) {
        return this.movieRepository.findById(movieId)
                .map(MovieMapper::toMovieDetails)
                .orElseThrow(() -> new MovieNotFoundException(movieId));
    }

    @Transactional
    public MovieDetails saveMovie(MovieDetails movieDetails) {
        var movie = this.movieRepository.save(MovieMapper.toMovie(movieDetails));
        this.eventPublisher.publishEvent(MovieMapper.toMovieAddedEvent(movie));
        return MovieMapper.toMovieDetails(movie);
    }
}
