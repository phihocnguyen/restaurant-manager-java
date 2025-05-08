package com.restaurant.backend.domains.dto.Recipe.dto;

import com.restaurant.backend.domains.dto.MenuItem.dto.CreateMenuItemDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMenuItemWithManyRecipesDto {
    private CreateMenuItemDto menuItem;
    private CreateManyRecipesDto recipes;
}
