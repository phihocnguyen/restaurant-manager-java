package com.restaurant.backend.domains.dto.Employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDto {
    private Integer id;

    private String empName;

    private String empAddr;

    private String empPhone;

    private String empCccd;

    private String empRole;

    private Instant empSdate;

    private Integer empWorkedDays;

    private BigDecimal empSalary;

    private Boolean isdeleted = false;
}
