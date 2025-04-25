package com.restaurant.backend.services;

import com.restaurant.backend.domains.dto.Ingredient.IngredientDto;
import com.restaurant.backend.domains.dto.Ingredient.dto.CreateIngreDto;
import com.restaurant.backend.domains.entities.Ingredient;

import java.util.List;
import java.util.Optional;

public interface IngredientService {
    public IngredientDto createIngredient(CreateIngreDto dto);
    public IngredientDto getIngredientById(int id);
    public List<IngredientDto> getAllIngredients();
    public IngredientDto updateIngredient(int id, CreateIngreDto dto);
    public IngredientDto partialUpdateIngredient(int id, CreateIngreDto dto);
    public boolean softDeleteIngredient(int id);
}
