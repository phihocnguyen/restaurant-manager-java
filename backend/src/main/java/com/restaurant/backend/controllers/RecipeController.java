package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.Recipe.RecipeDto;
import com.restaurant.backend.domains.dto.Recipe.dto.*;
import com.restaurant.backend.services.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/one")
    public ResponseEntity<RecipeDto> addOneRecipeWithMenu(
            @RequestPart("data") CreateMenuItemWithOneRecipeDto body,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        return ResponseEntity.ok(recipeService.addOneRecipeWithMenu(body, image));
    }

    @PostMapping("/many")
    public ResponseEntity<List<RecipeDto>> addManyRecipesWithMenu(
            @RequestPart("data") CreateMenuItemWithManyRecipesDto body,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        return ResponseEntity.ok(recipeService.addManyRecipesWithMenu(body, image));
    }

    @PostMapping("/one/{menuItemId}")
    public ResponseEntity<RecipeDto> addOneRecipeToMenuItem(@PathVariable int menuItemId, @RequestBody CreateRecipeDto dto) {
        RecipeDto saved = recipeService.addOneToExisting(menuItemId, dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PostMapping("/many/{menuItemId}")
    public ResponseEntity<List<RecipeDto>> addManyRecipesToMenuItem(@PathVariable int menuItemId, @RequestBody CreateManyRecipesDto dto) {
        List<RecipeDto> saved = recipeService.addManyToExisting(menuItemId, dto);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @GetMapping("/{menuItemId}")
    public ResponseEntity<List<RecipeDto>> getAllRecipes(@PathVariable int menuItemId) {
        List<RecipeDto> all = recipeService.getAllByMenuItemId(menuItemId);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PutMapping("/{menuItemId}")
    public ResponseEntity<List<RecipeDto>> updateAllRecipes(@PathVariable int menuItemId, @RequestBody CreateManyRecipesDto dto) {
        List<RecipeDto> updated = recipeService.updateAll(menuItemId, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PatchMapping("/{menuItemId}/{ingreId}")
    public ResponseEntity<RecipeDto> updateOneRecipe(@PathVariable int menuItemId, @PathVariable int ingreId, @RequestBody CreateRecipeDto dto) {
        RecipeDto updated = recipeService.updateOne(menuItemId, ingreId, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{menuItemId}/{ingreId}")
    public ResponseEntity<Boolean> deleteOneRecipe(@PathVariable int menuItemId, @PathVariable int ingreId) {
        boolean deleted = recipeService.deleteOne(menuItemId, ingreId);
        return new ResponseEntity<>(deleted, deleted ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{menuItemId}")
    public ResponseEntity<Boolean> deleteAllRecipes(@PathVariable int menuItemId) {
        boolean deleted = recipeService.deleteAll(menuItemId);
        return new ResponseEntity<>(deleted, deleted ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}