package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.entities.MenuItem;
import com.restaurant.backend.domains.entities.Recipe;
import com.restaurant.backend.domains.entities.RecipeId;
import com.restaurant.backend.repositories.RecipeRepository;
import com.restaurant.backend.services.RecipeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe save(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public List<Recipe> saveAll(List<Recipe> recipes) {
        return recipeRepository.saveAll(recipes);
    }

    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    public List<Recipe> findAllByItemId(int itemId) {
        List<Recipe> allRecipes = this.findAll();
        return allRecipes.stream().filter(r -> r.getItem().getId() == itemId).collect(Collectors.toList());
    }

    public Optional<Recipe> findById(RecipeId recipeId) {
        return recipeRepository.findById(recipeId);
    }

    public void deleteAll(List<Recipe> oldRecipes) {
        recipeRepository.deleteAll(oldRecipes);
    }

    public void deleteById(RecipeId recipeId) {
        recipeRepository.deleteById(recipeId);
    }
}
