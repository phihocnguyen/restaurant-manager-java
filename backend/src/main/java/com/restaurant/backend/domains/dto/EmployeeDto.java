package com.restaurant.backend.domains.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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
