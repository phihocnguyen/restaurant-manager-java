package com.restaurant.backend.mappers.impl;

import com.restaurant.backend.domains.dto.IngredientDto;
import com.restaurant.backend.domains.entities.Ingredient;
import com.restaurant.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class IngredientMapper implements Mapper<Ingredient, IngredientDto> {
    private ModelMapper modelMapper = new ModelMapper();
    public IngredientMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public IngredientDto mapFrom(Ingredient ingredient) {
        return modelMapper.map(ingredient, IngredientDto.class);
    }

    @Override
    public Ingredient mapTo(IngredientDto ingredientDto) {
        return modelMapper.map(ingredientDto, Ingredient.class);
    }
}
