package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.dto.Ingredient.IngredientDto;
import com.restaurant.backend.domains.dto.Ingredient.dto.CreateIngreDto;
import com.restaurant.backend.domains.entities.Ingredient;
import com.restaurant.backend.mappers.impl.IngredientMapper;
import com.restaurant.backend.repositories.IngredientRepository;
import com.restaurant.backend.services.IngredientService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;

    public IngredientServiceImpl(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
    }

    public IngredientDto createIngredient(CreateIngreDto dto) {
        Ingredient entity = ingredientMapper.mapTo(dto);
        return ingredientMapper.mapFrom(ingredientRepository.save(entity));
    }

    public IngredientDto getIngredientById(int id) {
        Optional<Ingredient> found = ingredientRepository.findById(id);
        return found.isPresent() ? ingredientMapper.mapFrom(found.get()) : null;
    }

    public List<IngredientDto> getAllIngredients() {
        return ingredientRepository.findAll().stream()
                .filter(i -> !Boolean.TRUE.equals(i.getIsdeleted()))
                .map(ingredientMapper::mapFrom)
                .collect(Collectors.toList());
    }

    public IngredientDto updateIngredient(int id, CreateIngreDto dto) {
        Optional<Ingredient> found = ingredientRepository.findById(id);
        if (!found.isPresent())
            return null;

        Ingredient updated = ingredientMapper.mapTo(dto);
        updated.setId(id);
        return ingredientMapper.mapFrom(ingredientRepository.save(updated));
    }

    public IngredientDto partialUpdateIngredient(int id, CreateIngreDto dto) {
        Optional<Ingredient> found = ingredientRepository.findById(id);
        if (!found.isPresent())
            return null;

        Ingredient ingre = found.get();
        if (dto.getIngreName() != null)
            ingre.setIngreName(dto.getIngreName());
        if (dto.getIngrePrice() != null)
            ingre.setIngrePrice(dto.getIngrePrice());
        if (dto.getInstockKg() != null)
            ingre.setInstockKg(dto.getInstockKg());
        if (dto.getIsdeleted() != null)
            ingre.setIsdeleted(dto.getIsdeleted());

        return ingredientMapper.mapFrom(ingredientRepository.save(ingre));
    }

    public boolean softDeleteIngredient(int id) {
        Optional<Ingredient> found = ingredientRepository.findById(id);
        if (!found.isPresent())
            return false;

        Ingredient ingre = found.get();
        ingre.setIsdeleted(true);
        ingredientRepository.save(ingre);
        return true;
    }

    public Optional<Ingredient> findById(int id) {
        return ingredientRepository.findById(id);
    }

}
