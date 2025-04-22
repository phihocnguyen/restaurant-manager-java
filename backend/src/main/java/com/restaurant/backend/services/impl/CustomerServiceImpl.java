package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.entities.Customer;
import com.restaurant.backend.repositories.CustomerRepository;
import com.restaurant.backend.services.CustomerService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Optional<Customer> findById(Integer id) {
        return customerRepository.findById(id);
    }
}
