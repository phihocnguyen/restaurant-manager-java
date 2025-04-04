package com.restaurant.backend.services.impl;

import com.restaurant.backend.repositories.CustomerRepository;
import com.restaurant.backend.services.CustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
}
