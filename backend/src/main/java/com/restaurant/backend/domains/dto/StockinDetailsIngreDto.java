package com.restaurant.backend.domains.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockinDetailsIngreDto {
    private StockinDto sto;

    private IngredientDto ingre;

    private Double quantityKg;

    private BigDecimal totalCprice;

    private BigDecimal cprice;
}
