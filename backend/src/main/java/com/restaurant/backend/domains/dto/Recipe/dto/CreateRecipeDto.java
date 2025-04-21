package com.restaurant.backend.domains.dto.Recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRecipeDto {
    private int ingreId;
    private Double ingreQuantityKg;
}
