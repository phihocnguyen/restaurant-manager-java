package com.restaurant.backend.mappers.impl;

import com.restaurant.backend.domains.dto.Recipe.RecipeDto;
import com.restaurant.backend.domains.entities.Recipe;
import com.restaurant.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RecipeMapper implements Mapper<Recipe, RecipeDto> {
    private ModelMapper modelMapper = new ModelMapper();
    public RecipeMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    @Override
    public RecipeDto mapFrom(Recipe recipe) {
        return null;
    }

    @Override
    public Recipe mapTo(RecipeDto recipeDto) {
        return null;
    }
}
