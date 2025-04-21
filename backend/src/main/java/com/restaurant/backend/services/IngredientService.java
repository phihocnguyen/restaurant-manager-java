package com.restaurant.backend.services;

import com.restaurant.backend.domains.entities.Ingredient;

import java.util.List;
import java.util.Optional;

public interface IngredientService {
    public Ingredient save(Ingredient ingredient);
    public Optional<Ingredient> findById(int id);
    public List<Ingredient> findAll();
}
