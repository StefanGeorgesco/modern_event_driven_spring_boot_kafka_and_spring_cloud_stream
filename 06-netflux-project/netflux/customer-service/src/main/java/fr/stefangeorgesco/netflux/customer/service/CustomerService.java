package fr.stefangeorgesco.netflux.customer.service;

import fr.stefangeorgesco.netflux.customer.dto.CustomerDetails;
import fr.stefangeorgesco.netflux.customer.dto.GenreUpdateRequest;
import fr.stefangeorgesco.netflux.customer.exception.CustomerNotFoundException;
import fr.stefangeorgesco.netflux.customer.mapper.CustomerMapper;
import fr.stefangeorgesco.netflux.customer.repository.CustomerRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CustomerService(CustomerRepository customerRepository, ApplicationEventPublisher eventPublisher) {
        this.customerRepository = customerRepository;
        this.eventPublisher = eventPublisher;
    }

    public CustomerDetails getCustomer(Integer customerId) {
        return this.customerRepository.findById(customerId)
                .map(CustomerMapper::toCustomerDetails)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    @Transactional
    public void updateCustomerGenre(Integer customerId, GenreUpdateRequest request) {
        var customer = this.customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        customer.setFavoriteGenre(request.favoriteGenre());
        this.eventPublisher.publishEvent(CustomerMapper.toGenreUpdatedEvent(customerId, request.favoriteGenre()));
    }
}
