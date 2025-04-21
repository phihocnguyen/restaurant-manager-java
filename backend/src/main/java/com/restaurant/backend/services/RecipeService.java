package com.restaurant.backend.services;

import com.restaurant.backend.domains.entities.MenuItem;
import com.restaurant.backend.domains.entities.Recipe;
import com.restaurant.backend.domains.entities.RecipeId;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    public Recipe save(Recipe recipe);
    public List<Recipe> saveAll(List<Recipe> recipes);
    public List<Recipe> findAll();
    public List<Recipe> findAllByItemId(int itemId);
    public Optional<Recipe> findById(RecipeId recipeId);
    public void deleteAll(List<Recipe> oldRecipes);
    public void deleteById(RecipeId recipeId);
}
