package com.restaurant.backend.mappers.impl;

import com.restaurant.backend.domains.dto.Employee.EmployeeDto;
import com.restaurant.backend.domains.entities.Employee;
import com.restaurant.backend.mappers.Mapper;
import com.restaurant.backend.repositories.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper implements Mapper<Employee, EmployeeDto> {
    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;

    public EmployeeMapper(ModelMapper modelMapper, AccountRepository accountRepository) {
        this.modelMapper = modelMapper;
        this.accountRepository = accountRepository;
    }

    @Override
    public EmployeeDto mapFrom(Employee employee) {
        EmployeeDto dto = modelMapper.map(employee, EmployeeDto.class);
        // Find and map only the email from the corresponding account
        accountRepository.findOneByAccPhone(employee.getPhone())
                .ifPresent(account -> dto.setEmail(account.getAccEmail()));
        return dto;
    }

    @Override
    public Employee mapTo(EmployeeDto employeeDto) {
        return modelMapper.map(employeeDto, Employee.class);
    }
}
