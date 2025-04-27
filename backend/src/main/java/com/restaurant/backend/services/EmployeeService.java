package com.restaurant.backend.services;

import com.restaurant.backend.domains.dto.Employee.EmployeeDto;
import com.restaurant.backend.domains.dto.Employee.dto.CreateEmployeeDto;
import com.restaurant.backend.domains.entities.Employee;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    ResponseEntity<EmployeeDto> createEmployee(CreateEmployeeDto dto);

    Optional<Employee> findById(Integer id);

    ResponseEntity<EmployeeDto> getEmployeeById(Integer id);

    ResponseEntity<EmployeeDto> getEmployeeByCccd(String cccd);

    ResponseEntity<List<EmployeeDto>> getAllEmployees();

    ResponseEntity<List<EmployeeDto>> getEmployeesByRole(String role);

    ResponseEntity<EmployeeDto> updateEmployee(Integer id, CreateEmployeeDto dto);

    ResponseEntity<EmployeeDto> updateEmployeeRole(Integer id, String role);

    ResponseEntity<EmployeeDto> updateEmployeeSalary(Integer id, Double salary);

    ResponseEntity<Boolean> deleteEmployee(Integer id);
}
