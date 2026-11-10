package fr.stefangeorgesco.netflux.recommendation.controller;

import fr.stefangeorgesco.netflux.recommendation.dto.MovieRecommendations;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @GetMapping("/{customerId}")
    public List<MovieRecommendations> getRecommendations(@PathVariable Integer customerId) {
        return Collections.emptyList();
    }

    @GetMapping(value = "/{customerId}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MovieRecommendations> getRecommendationStream(@PathVariable Integer customerId) {
        return Flux.empty();
    }
}
