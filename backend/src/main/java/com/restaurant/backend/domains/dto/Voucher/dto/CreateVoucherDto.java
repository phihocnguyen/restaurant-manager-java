package com.restaurant.backend.domains.dto.Voucher.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateVoucherDto {
    @Min(0)
    private int pointsRequired;

    @NotNull
    @Min(0)
    private BigDecimal discountValue;

    private boolean isPercentage;

    @NotBlank
    private String name;

    private String description;

    private LocalDate expiryDate;

    private boolean isActive = true;
} 