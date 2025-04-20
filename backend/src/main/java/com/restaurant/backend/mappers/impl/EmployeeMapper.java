package com.restaurant.backend.mappers.impl;

import com.restaurant.backend.domains.dto.Employee.EmployeeDto;
import com.restaurant.backend.domains.entities.Employee;
import com.restaurant.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper implements Mapper<Employee, EmployeeDto> {
    private ModelMapper modelMapper = new ModelMapper();
    public EmployeeMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public EmployeeDto mapFrom(Employee employee) {
        return modelMapper.map(employee, EmployeeDto.class);
    }

    @Override
    public Employee mapTo(EmployeeDto employeeDto) {
        return modelMapper.map(employeeDto, Employee.class);
    }
}
