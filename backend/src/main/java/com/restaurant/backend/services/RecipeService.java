package com.restaurant.backend.services;

import com.restaurant.backend.domains.dto.Recipe.RecipeDto;
import com.restaurant.backend.domains.dto.Recipe.dto.CreateManyRecipesDto;
import com.restaurant.backend.domains.dto.Recipe.dto.CreateMenuItemWithManyRecipesDto;
import com.restaurant.backend.domains.dto.Recipe.dto.CreateMenuItemWithOneRecipeDto;
import com.restaurant.backend.domains.dto.Recipe.dto.CreateRecipeDto;
import com.restaurant.backend.domains.entities.MenuItem;
import com.restaurant.backend.domains.entities.Recipe;
import com.restaurant.backend.domains.entities.RecipeId;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    public RecipeDto addOneRecipeWithMenu(CreateMenuItemWithOneRecipeDto body);
    public Recipe save(Recipe recipe);
    public List<RecipeDto> addManyRecipesWithMenu(CreateMenuItemWithManyRecipesDto body);
    public RecipeDto addOneToExisting(int menuItemId, CreateRecipeDto dto);
    public List<RecipeDto> addManyToExisting(int menuItemId, CreateManyRecipesDto dto);
    public List<RecipeDto> getAllByMenuItemId(int menuItemId);
    public List<RecipeDto> updateAll(int menuItemId, CreateManyRecipesDto dto);
    public RecipeDto updateOne(int menuItemId, int ingreId, CreateRecipeDto dto);
    public boolean deleteOne(int menuItemId, int ingreId);
    public boolean deleteAll(int menuItemId);
    public Optional<Recipe> findById(RecipeId recipeId);
}
