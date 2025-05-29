package com.restaurant.backend.domains.dto.Voucher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherDto {
    private Integer id;
    private int pointsRequired;
    private BigDecimal discountValue;
    private boolean isPercentage;
    private String name;
    private String description;
    private LocalDate expiryDate;
    private boolean isActive;
} 