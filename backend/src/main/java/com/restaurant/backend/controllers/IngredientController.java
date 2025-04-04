package com.restaurant.backend.controllers;

import com.restaurant.backend.services.IngredientService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IngredientController {
    private final IngredientService ingredientService;
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }
}
