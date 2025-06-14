package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.dto.Customer.CustomerDto;
import com.restaurant.backend.domains.dto.Customer.CreateCustomerDto;
import com.restaurant.backend.domains.entities.Customer;
import com.restaurant.backend.repositories.CustomerRepository;
import com.restaurant.backend.services.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDto getCustomerById(Integer id) {
        return customerRepository.findById(id)
                .map(this::convertToDto)
                .orElse(null);
    }

    @Override
    public CustomerDto getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email);
        if (customer != null) {
            return convertToDto(customer);
        }
        return null;
    }

    @Override
    public CustomerDto createCustomer(CreateCustomerDto createCustomerDto) {
        Customer customer = new Customer();
        customer.setName(createCustomerDto.getName());
        customer.setCccd(createCustomerDto.getCccd());
        customer.setEmail(createCustomerDto.getEmail());
        customer.setPhone(createCustomerDto.getPhone());
        customer.setAddress(createCustomerDto.getAddress());
        customer.setIsdeleted(false);
        customer.setIsvip(createCustomerDto.getIsvip());

        Customer savedCustomer = customerRepository.save(customer);
        return convertToDto(savedCustomer);
    }

    @Override
    public CustomerDto updateCustomer(Integer id, CustomerDto customerDto) {
        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    existingCustomer.setName(customerDto.getName());
                    existingCustomer.setCccd(customerDto.getCccd());
                    existingCustomer.setEmail(customerDto.getEmail());
                    existingCustomer.setPhone(customerDto.getPhone());
                    existingCustomer.setAddress(customerDto.getAddress());
                    existingCustomer.setIsdeleted(customerDto.getIsdeleted());
                    existingCustomer.setIsvip(customerDto.getIsvip());
                    return convertToDto(customerRepository.save(existingCustomer));
                })
                .orElse(null);
    }

    @Override
    public void deleteCustomer(Integer id) {
        customerRepository.findById(id).ifPresent(customer -> {
            customer.setIsdeleted(true);
            customerRepository.save(customer);
        });
    }

    @Override
    public boolean existsByCccd(String cccd) {
        return customerRepository.existsByCccd(cccd);
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return customerRepository.existsByPhone(phone);
    }

    private CustomerDto convertToDto(Customer customer) {
        CustomerDto dto = new CustomerDto();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setCccd(customer.getCccd());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setAddress(customer.getAddress());
        dto.setIsdeleted(customer.getIsdeleted());
        dto.setIsvip(customer.getIsvip());
        return dto;
    }
}
