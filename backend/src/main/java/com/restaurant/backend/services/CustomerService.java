package com.restaurant.backend.services;

import com.restaurant.backend.domains.entities.Customer;

import java.util.Optional;

public interface CustomerService {
    public Optional<Customer> findById(Integer id);
}
