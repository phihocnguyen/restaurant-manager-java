package com.restaurant.backend.services;

import com.restaurant.backend.domains.entities.Ingredient;

import java.util.List;

public interface IngredientService {
    public Ingredient save(Ingredient ingredient);
    public Ingredient findById(int id);
    public List<Ingredient> findAll();
}
