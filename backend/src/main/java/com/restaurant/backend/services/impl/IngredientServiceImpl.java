package com.restaurant.backend.services.impl;

import com.restaurant.backend.repositories.IngredientRepository;
import com.restaurant.backend.services.IngredientService;
import org.springframework.stereotype.Service;

@Service
public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;
    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }
}
