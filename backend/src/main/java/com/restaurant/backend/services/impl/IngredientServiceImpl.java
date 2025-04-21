package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.entities.Ingredient;
import com.restaurant.backend.repositories.IngredientRepository;
import com.restaurant.backend.services.IngredientService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;
    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }
    public Ingredient save(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }
    public Optional<Ingredient> findById(int id) {
        return this.ingredientRepository.findById(id);
    }
    public List<Ingredient> findAll() {
        return this.ingredientRepository.findAll()
                .stream().filter(ingredient -> ingredient.getIsdeleted() == false).collect(Collectors.toList());
    }
}
