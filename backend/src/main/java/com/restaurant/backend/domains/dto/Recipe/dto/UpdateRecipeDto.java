package com.restaurant.backend.domains.dto.Recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRecipeDto {
    private double ingreQuantityKg;
    private String recipeImg;
} 