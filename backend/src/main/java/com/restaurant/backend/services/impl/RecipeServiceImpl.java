package com.restaurant.backend.services.impl;

import com.restaurant.backend.repositories.RecipeRepository;
import com.restaurant.backend.services.RecipeService;
import org.springframework.stereotype.Service;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }
}
