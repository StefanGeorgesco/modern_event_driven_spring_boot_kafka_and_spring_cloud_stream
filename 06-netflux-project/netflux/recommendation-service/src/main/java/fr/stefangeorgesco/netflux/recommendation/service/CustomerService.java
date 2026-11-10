package fr.stefangeorgesco.netflux.recommendation.service;

import fr.stefangeorgesco.netflux.netflux.events.CustomerGenreUpdatedEvent;
import fr.stefangeorgesco.netflux.recommendation.dto.RecommendationEvents;
import fr.stefangeorgesco.netflux.recommendation.mapper.RecommendationMapper;
import fr.stefangeorgesco.netflux.recommendation.repository.CustomerGenreRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerGenreRepository customerGenreRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CustomerService(CustomerGenreRepository customerGenreRepository, ApplicationEventPublisher eventPublisher) {
        this.customerGenreRepository = customerGenreRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void updateGenre(CustomerGenreUpdatedEvent genreUpdatedEvent) {
        var entity = RecommendationMapper.toCustomerGenre(genreUpdatedEvent);
        customerGenreRepository.save(entity);
        eventPublisher.publishEvent(new RecommendationEvents.PersonalizedEvent(genreUpdatedEvent.customerId()));
    }
}
