package com.restaurant.backend.domains.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDto {
    private MenuItemDto item;

    private IngredientDto ingre;

    private Double ingreQuantityKg;
}
