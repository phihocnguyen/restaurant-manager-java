package com.restaurant.backend.domains.dto.Employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private Integer id;
    private String name;
    private String address;
    private String phone;
    private String cccd;
    private String role;
    private Instant startDate;
    private Integer workedDays;
    private BigDecimal salary;
    private Boolean isdeleted;
}
