package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.Recipe.RecipeDto;
import com.restaurant.backend.domains.dto.Recipe.dto.*;
import com.restaurant.backend.domains.dto.MenuItem.dto.CreateMenuItemDto;
import com.restaurant.backend.domains.dto.MenuItem.interfaces.ItemType;
import com.restaurant.backend.services.RecipeService;
import com.restaurant.backend.services.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    public RecipeController(RecipeService recipeService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    @PostMapping("/one")
    @Transactional
    public ResponseEntity<RecipeDto> addOneRecipeWithMenu(
            @RequestParam("itemType") String itemType,
            @RequestParam("itemName") String itemName,
            @RequestParam(value = "itemSprice", required = false) String itemSprice,
            @RequestParam(value = "instock", required = false) Double instock,
            @RequestParam(value = "isdeleted", required = false) Boolean isdeleted,
            @RequestParam("ingreId") Integer ingreId,
            @RequestParam("ingreQuantityKg") Double ingreQuantityKg,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        
        CreateMenuItemWithOneRecipeDto body = new CreateMenuItemWithOneRecipeDto();
        CreateMenuItemDto menuItem = new CreateMenuItemDto();
        menuItem.setItemType(ItemType.valueOf(itemType));
        menuItem.setItemName(itemName);
        if (itemSprice != null) menuItem.setItemSprice(new BigDecimal(itemSprice));
        if (instock != null) menuItem.setInstock(instock);
        if (isdeleted != null) menuItem.setIsdeleted(isdeleted);
        body.setMenuItem(menuItem);

        CreateRecipeDto recipe = new CreateRecipeDto();
        recipe.setIngreId(ingreId);
        recipe.setIngreQuantityKg(ingreQuantityKg);
        body.setRecipe(recipe);

        // Check and update ingredient stock
        if (!ingredientService.decreaseStock(ingreId, ingreQuantityKg)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không đủ nguyên liệu!");
        }

        try {
            RecipeDto result = recipeService.addOneRecipeWithMenu(body, image);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // Rollback stock update if menu creation fails
            ingredientService.increaseStock(ingreId, ingreQuantityKg);
            throw e;
        }
    }

    @PostMapping("/many")
    @Transactional
    public ResponseEntity<List<RecipeDto>> addManyRecipesWithMenu(
            @RequestParam("itemType") String itemType,
            @RequestParam("itemName") String itemName,
            @RequestParam(value = "itemSprice", required = false) String itemSprice,
            @RequestParam(value = "instock", required = false) Double instock,
            @RequestParam(value = "isdeleted", required = false) Boolean isdeleted,
            @RequestParam(value = "ingreIds[]") List<Integer> ingreIds,
            @RequestParam(value = "ingreQuantityKgs[]") List<Double> ingreQuantityKgs,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        
        if (ingreIds == null || ingreQuantityKgs == null || ingreIds.isEmpty() || ingreQuantityKgs.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thiếu thông tin nguyên liệu!");
        }

        CreateMenuItemWithManyRecipesDto body = new CreateMenuItemWithManyRecipesDto();
        CreateMenuItemDto menuItem = new CreateMenuItemDto();
        menuItem.setItemType(ItemType.valueOf(itemType));
        menuItem.setItemName(itemName);
        if (itemSprice != null) menuItem.setItemSprice(new BigDecimal(itemSprice));
        if (instock != null) menuItem.setInstock(instock);
        if (isdeleted != null) menuItem.setIsdeleted(isdeleted);
        body.setMenuItem(menuItem);

        CreateManyRecipesDto recipes = new CreateManyRecipesDto();
        List<CreateRecipeDto> recipeList = new ArrayList<>();
        for (int i = 0; i < ingreIds.size(); i++) {
            CreateRecipeDto recipe = new CreateRecipeDto();
            recipe.setIngreId(ingreIds.get(i));
            recipe.setIngreQuantityKg(ingreQuantityKgs.get(i));
            recipeList.add(recipe);
        }
        recipes.setIngredients(recipeList);
        body.setRecipes(recipes);

        // Check and update ingredient stocks
        for (int i = 0; i < ingreIds.size(); i++) {
            if (!ingredientService.decreaseStock(ingreIds.get(i), ingreQuantityKgs.get(i))) {
                // Rollback previous stock updates
                for (int j = 0; j < i; j++) {
                    ingredientService.increaseStock(ingreIds.get(j), ingreQuantityKgs.get(j));
                }
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không đủ nguyên liệu!");
            }
        }

        try {
            List<RecipeDto> result = recipeService.addManyRecipesWithMenu(body, image);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // Rollback all stock updates if menu creation fails
            for (int i = 0; i < ingreIds.size(); i++) {
                ingredientService.increaseStock(ingreIds.get(i), ingreQuantityKgs.get(i));
            }
            throw e;
        }
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