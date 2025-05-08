package com.restaurant.backend.domains.dto.Ingredient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngredientDto {
    private Integer id;

    private String ingreName;

    private Double instockKg = 0.0;

    private BigDecimal ingrePrice = BigDecimal.ZERO;

    private Boolean isdeleted = false;
}
