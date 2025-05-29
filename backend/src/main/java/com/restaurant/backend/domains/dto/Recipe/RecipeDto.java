package com.restaurant.backend.domains.dto.Recipe;

import com.restaurant.backend.domains.dto.Ingredient.IngredientDto;
import com.restaurant.backend.domains.dto.MenuItem.MenuItemDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {
    private int itemId;
    private int ingreId;
    private MenuItemDto item;
    private IngredientDto ingre;
    private double ingreQuantityKg;
    private String recipeImg;
}
