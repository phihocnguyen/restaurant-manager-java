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
    private Integer id;
    private Integer itemId;
    private Integer ingreId;
    private MenuItemDto menuItem;
    private IngredientDto ingredient;
    private Double ingreQuantityKg;
    private String recipeImg;
}
