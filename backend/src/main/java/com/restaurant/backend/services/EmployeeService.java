package com.restaurant.backend.services;

import com.restaurant.backend.domains.entities.Employee;

import java.util.Optional;

public interface EmployeeService {
    public Optional<Employee> findById(Integer id);
}
