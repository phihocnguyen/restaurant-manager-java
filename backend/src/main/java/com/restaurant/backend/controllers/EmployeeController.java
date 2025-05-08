package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.Employee.EmployeeDto;
import com.restaurant.backend.domains.dto.Employee.dto.CreateEmployeeDto;
import com.restaurant.backend.services.EmployeeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody CreateEmployeeDto dto) {
        return employeeService.createEmployee(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable Integer id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/cccd/{cccd}")
    public ResponseEntity<EmployeeDto> getEmployeeByCccd(@PathVariable String cccd) {
        return employeeService.getEmployeeByCccd(cccd);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByRole(@PathVariable String role) {
        return employeeService.getEmployeesByRole(role);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(
            @PathVariable Integer id,
            @RequestBody CreateEmployeeDto dto) {
        return employeeService.updateEmployee(id, dto);
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<EmployeeDto> updateEmployeeRole(
            @PathVariable Integer id,
            @RequestParam String role) {
        return employeeService.updateEmployeeRole(id, role);
    }

    @PatchMapping("/{id}/salary")
    public ResponseEntity<EmployeeDto> updateEmployeeSalary(
            @PathVariable Integer id,
            @RequestParam Double salary) {
        return employeeService.updateEmployeeSalary(id, salary);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteEmployee(@PathVariable Integer id) {
        return employeeService.deleteEmployee(id);
    }
}
