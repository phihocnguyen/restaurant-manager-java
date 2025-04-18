package com.restaurant.backend.controllers;

import com.restaurant.backend.services.CustomerService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
}
