package com.restaurant.backend.services;

import com.restaurant.backend.domains.dto.Recipe.RecipeDto;
import com.restaurant.backend.domains.dto.Recipe.dto.CreateManyRecipesDto;
import com.restaurant.backend.domains.dto.Recipe.dto.CreateMenuItemWithManyRecipesDto;
import com.restaurant.backend.domains.dto.Recipe.dto.CreateMenuItemWithOneRecipeDto;
import com.restaurant.backend.domains.dto.Recipe.dto.CreateRecipeDto;
import com.restaurant.backend.domains.dto.Recipe.dto.UpdateRecipeDto;
import com.restaurant.backend.domains.entities.MenuItem;
import com.restaurant.backend.domains.entities.Recipe;
import com.restaurant.backend.domains.entities.RecipeId;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface RecipeService {
    public RecipeDto addOneRecipeWithMenu(CreateMenuItemWithOneRecipeDto body);
    public RecipeDto addOneRecipeWithMenu(CreateMenuItemWithOneRecipeDto body, MultipartFile image) throws IOException;
    public Recipe save(Recipe recipe);
    public List<RecipeDto> addManyRecipesWithMenu(CreateMenuItemWithManyRecipesDto body);
    public List<RecipeDto> addManyRecipesWithMenu(CreateMenuItemWithManyRecipesDto body, MultipartFile image) throws IOException;
    public RecipeDto addOneToExisting(int menuItemId, CreateRecipeDto dto);
    public List<RecipeDto> addManyToExisting(int menuItemId, CreateManyRecipesDto dto);
    public List<RecipeDto> getAllByMenuItemId(int menuItemId);
    public List<RecipeDto> updateAll(int menuItemId, CreateManyRecipesDto dto);
    public RecipeDto updateOne(int menuItemId, int ingreId, CreateRecipeDto dto);
    public boolean deleteOne(int menuItemId, int ingreId);
    public boolean deleteAll(int menuItemId);
    public Optional<Recipe> findById(RecipeId recipeId);
    RecipeDto createRecipe(CreateRecipeDto dto, MultipartFile image) throws IOException;
    RecipeDto getRecipeById(int itemId, int ingreId);
    List<RecipeDto> getAllRecipes();
    RecipeDto updateRecipe(int itemId, int ingreId, UpdateRecipeDto dto, MultipartFile image) throws IOException;
    RecipeDto partialUpdateRecipe(int itemId, int ingreId, UpdateRecipeDto dto);
    boolean deleteRecipe(int itemId, int ingreId);
}
