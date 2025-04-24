package com.restaurant.backend.domains.dto.Ingredient.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateIngreDto {
    private String ingreName;

    private Double instockKg = 0.0;

    private BigDecimal ingrePrice = BigDecimal.ZERO;

    private Boolean isdeleted = false;
}
