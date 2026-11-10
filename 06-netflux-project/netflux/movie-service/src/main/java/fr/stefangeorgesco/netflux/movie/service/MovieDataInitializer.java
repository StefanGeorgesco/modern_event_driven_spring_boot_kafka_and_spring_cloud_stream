package fr.stefangeorgesco.netflux.movie.service;

import fr.stefangeorgesco.netflux.movie.dto.MovieDetails;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service // import the movies for demo purposes
@ConditionalOnProperty(name = "app.import-movies", havingValue = "true")
public class MovieDataInitializer implements CommandLineRunner {

    private final MovieService movieService;
    private final Resource resource;

    public MovieDataInitializer(MovieService movieService, @Value("classpath:movies.jsonl") Resource resource) {
        this.movieService = movieService;
        this.resource = resource;
    }

    @Override
    public void run(String @NonNull ... args) {
        Thread.ofVirtual().start(this::loadMovies);
    }

    private void loadMovies() {
        var mapper = JsonMapper.shared();
        try (var reader = new BufferedReader(new InputStreamReader(this.resource.getInputStream()))) {
            reader.lines()
                    .map(line -> mapper.readValue(line, MovieDetails.class))
                    .forEach(movieDetails -> {
                        this.movieService.saveMovie(movieDetails);
                        sleep();
                    });
        } catch (IOException e) {
            throw new RuntimeException("Failed to read movies from resource", e);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
