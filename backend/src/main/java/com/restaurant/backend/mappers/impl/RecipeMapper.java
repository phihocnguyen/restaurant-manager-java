package com.restaurant.backend.mappers.impl;

import com.restaurant.backend.domains.dto.Recipe.RecipeDto;
import com.restaurant.backend.domains.dto.Recipe.dto.CreateRecipeDto;
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
        return modelMapper.map(recipe, RecipeDto.class);
    }

    @Override
    public Recipe mapTo(RecipeDto recipeDto) {
        return modelMapper.map(recipeDto, Recipe.class);
    }

    // map manually
    public Recipe mapTo(CreateRecipeDto createRecipeDto) {
        return Recipe.builder()
                .id(null) // set id later!
                .ingreQuantityKg(createRecipeDto.getIngreQuantityKg())
                .build();
    }
}
