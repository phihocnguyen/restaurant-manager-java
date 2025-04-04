package com.restaurant.backend.mappers.impl;

import com.restaurant.backend.domains.dto.CustomerDto;
import com.restaurant.backend.domains.entities.Customer;
import com.restaurant.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper implements Mapper<CustomerDto, Customer> {
    private ModelMapper modelMapper = new ModelMapper();
    public CustomerMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Customer mapFrom(CustomerDto customerDto) {
        return modelMapper.map(customerDto, Customer.class);
    }

    @Override
    public CustomerDto mapTo(Customer customer) {
        return modelMapper.map(customer, CustomerDto.class);
    }
}
