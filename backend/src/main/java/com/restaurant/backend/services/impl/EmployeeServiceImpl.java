package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.dto.Employee.EmployeeDto;
import com.restaurant.backend.domains.dto.Employee.dto.CreateEmployeeDto;
import com.restaurant.backend.domains.entities.Employee;
import com.restaurant.backend.mappers.impl.EmployeeMapper;
import com.restaurant.backend.repositories.EmployeeRepository;
import com.restaurant.backend.services.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public Optional<Employee> findById(Integer id) {
        return employeeRepository.findById(id);
    }

    @Override
    public ResponseEntity<EmployeeDto> createEmployee(CreateEmployeeDto dto) {
        if (employeeRepository.findByCccd(dto.getCccd()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setAddress(dto.getAddress());
        employee.setPhone(dto.getPhone());
        employee.setCccd(dto.getCccd());
        employee.setRole(dto.getRole());
        employee.setStartDate(dto.getStartDate());
        employee.setWorkedDays(dto.getWorkedDays());
        employee.setSalary(dto.getSalary());
        employee.setIsdeleted(false);

        Employee saved = employeeRepository.save(employee);
        return new ResponseEntity<>(employeeMapper.mapFrom(saved), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<EmployeeDto> getEmployeeById(Integer id) {
        return employeeRepository.findById(id)
                .map(employee -> new ResponseEntity<>(employeeMapper.mapFrom(employee), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<EmployeeDto> getEmployeeByCccd(String cccd) {
        return employeeRepository.findByCccd(cccd)
                .map(employee -> new ResponseEntity<>(employeeMapper.mapFrom(employee), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<Employee> employees = employeeRepository.findByIsdeletedFalse();
        List<EmployeeDto> dtos = employees.stream()
                .map(employeeMapper::mapFrom)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<EmployeeDto>> getEmployeesByRole(String role) {
        List<Employee> employees = employeeRepository.findByRoleAndIsdeletedFalse(role);
        List<EmployeeDto> dtos = employees.stream()
                .map(employeeMapper::mapFrom)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EmployeeDto> updateEmployee(Integer id, CreateEmployeeDto dto) {
        return employeeRepository.findById(id)
                .map(employee -> {
                    employee.setName(dto.getName());
                    employee.setAddress(dto.getAddress());
                    employee.setPhone(dto.getPhone());
                    employee.setCccd(dto.getCccd());
                    employee.setRole(dto.getRole());
                    employee.setStartDate(dto.getStartDate());
                    employee.setWorkedDays(dto.getWorkedDays());
                    employee.setSalary(dto.getSalary());

                    Employee updated = employeeRepository.save(employee);
                    return new ResponseEntity<>(employeeMapper.mapFrom(updated), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<EmployeeDto> updateEmployeeRole(Integer id, String role) {
        return employeeRepository.findById(id)
                .map(employee -> {
                    employee.setRole(role);
                    Employee updated = employeeRepository.save(employee);
                    return new ResponseEntity<>(employeeMapper.mapFrom(updated), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<EmployeeDto> updateEmployeeSalary(Integer id, Double salary) {
        return employeeRepository.findById(id)
                .map(employee -> {
                    employee.setSalary(BigDecimal.valueOf(salary));
                    Employee updated = employeeRepository.save(employee);
                    return new ResponseEntity<>(employeeMapper.mapFrom(updated), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<Boolean> deleteEmployee(Integer id) {
        return employeeRepository.findById(id)
                .map(employee -> {
                    employee.setIsdeleted(true);
                    employeeRepository.save(employee);
                    return new ResponseEntity<>(true, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(false, HttpStatus.NOT_FOUND));
    }
}
