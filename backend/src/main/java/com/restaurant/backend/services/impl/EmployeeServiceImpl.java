package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.entities.Employee;
import com.restaurant.backend.repositories.EmployeeRepository;
import com.restaurant.backend.services.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Optional<Employee> findById(Integer id) {
        return employeeRepository.findById(id);
    }
}
