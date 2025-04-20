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

    @NotNull
    private Double instockKg;

    private BigDecimal ingrePrice;

    private Boolean isdeleted = false;
}
