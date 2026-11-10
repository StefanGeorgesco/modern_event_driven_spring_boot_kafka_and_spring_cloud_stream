package fr.stefangeorgesco.netflux.customer.service;

import fr.stefangeorgesco.netflux.customer.dto.CustomerDetails;
import fr.stefangeorgesco.netflux.customer.dto.GenreUpdateRequest;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    public CustomerDetails getCustomer(Integer customerId) {
        return null;
    }

    public void updateCustomerGenre(Integer customerId, GenreUpdateRequest request) {
        // TO DO
    }
}
