package com.restaurant.backend.domains.dto.Recipe;

import com.restaurant.backend.domains.dto.Ingredient.IngredientDto;
import com.restaurant.backend.domains.dto.MenuItem.MenuItemDto;
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
