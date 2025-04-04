package com.restaurant.backend.domains.dto;

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
public class ReceiptDto {
    private Integer id;

    private EmployeeDto emp;

    private CustomerDto cus;

    private DiningTableDto tab;

    private Instant recTime;

    private BigDecimal recPay;

    private Boolean isdeleted = false;
}
