package com.restaurant.backend.services;

import com.restaurant.backend.domains.entities.Customer;
import com.restaurant.backend.domains.dto.Customer.CustomerDto;
import com.restaurant.backend.domains.dto.Customer.CreateCustomerDto;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    public Optional<Customer> findById(Integer id);

    public List<CustomerDto> getAllCustomers();

    public CustomerDto getCustomerById(Integer id);

    public CustomerDto createCustomer(CreateCustomerDto createCustomerDto);

    public CustomerDto updateCustomer(Integer id, CustomerDto customerDto);

    public void deleteCustomer(Integer id);

    public boolean existsByCccd(String cccd);

    public boolean existsByEmail(String email);

    public boolean existsByPhone(String phone);
}
