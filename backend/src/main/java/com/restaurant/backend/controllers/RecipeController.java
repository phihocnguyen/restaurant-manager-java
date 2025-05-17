package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.Recipe.RecipeDto;
import com.restaurant.backend.domains.dto.Recipe.dto.*;
import com.restaurant.backend.services.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/recipes/one")
    public ResponseEntity<RecipeDto> addOneRecipe(@RequestBody CreateMenuItemWithOneRecipeDto body) {
        RecipeDto saved = recipeService.addOneRecipeWithMenu(body);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PostMapping("/recipes/many")
    public ResponseEntity<List<RecipeDto>> addManyRecipes(@RequestBody CreateMenuItemWithManyRecipesDto body) {
        List<RecipeDto> saved = recipeService.addManyRecipesWithMenu(body);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @PostMapping("/recipes/one/{menuItemId}")
    public ResponseEntity<RecipeDto> addOneRecipeToMenuItem(@PathVariable int menuItemId,
            @RequestBody CreateRecipeDto dto) {
        RecipeDto saved = recipeService.addOneToExisting(menuItemId, dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PostMapping("/recipes/many/{menuItemId}")
    public ResponseEntity<List<RecipeDto>> addManyRecipesToMenuItem(@PathVariable int menuItemId,
            @RequestBody CreateManyRecipesDto dto) {
        List<RecipeDto> saved = recipeService.addManyToExisting(menuItemId, dto);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @GetMapping("/recipes/{menuItemId}")
    public ResponseEntity<List<RecipeDto>> getAllRecipes(@PathVariable int menuItemId) {
        List<RecipeDto> all = recipeService.getAllByMenuItemId(menuItemId);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PutMapping("/recipes/{menuItemId}")
    public ResponseEntity<List<RecipeDto>> updateAllRecipes(@PathVariable int menuItemId,
            @RequestBody CreateManyRecipesDto dto) {
        List<RecipeDto> updated = recipeService.updateAll(menuItemId, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PatchMapping("/recipes/{menuItemId}/{ingreId}")
    public ResponseEntity<RecipeDto> updateOneRecipe(@PathVariable int menuItemId, @PathVariable int ingreId,
            @RequestBody CreateRecipeDto dto) {
        RecipeDto updated = recipeService.updateOne(menuItemId, ingreId, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/recipes/{menuItemId}/{ingreId}")
    public ResponseEntity<Boolean> deleteOneRecipe(@PathVariable int menuItemId, @PathVariable int ingreId) {
        boolean deleted = recipeService.deleteOne(menuItemId, ingreId);
        return new ResponseEntity<>(deleted, deleted ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/recipes/{menuItemId}")
    public ResponseEntity<Boolean> deleteAllRecipes(@PathVariable int menuItemId) {
        boolean deleted = recipeService.deleteAll(menuItemId);
        return new ResponseEntity<>(deleted, deleted ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}