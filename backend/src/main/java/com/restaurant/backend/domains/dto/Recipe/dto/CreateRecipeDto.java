package com.restaurant.backend.domains.dto.Recipe.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRecipeDto {
    @NotNull
    private Integer itemId;
    
    @NotNull
    private Integer ingreId;
    
    @NotNull
    @Positive
    private Double ingreQuantityKg;
    
    private String recipeImg;
}
