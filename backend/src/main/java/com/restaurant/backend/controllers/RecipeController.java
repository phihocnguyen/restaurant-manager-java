package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.MenuItem.dto.CreateMenuItemDto;
import com.restaurant.backend.domains.dto.Recipe.RecipeDto;
import com.restaurant.backend.domains.dto.Recipe.dto.CreateManyRecipeDto;
import com.restaurant.backend.domains.dto.Recipe.dto.CreateMenuItemWithManyRecipesDto;
import com.restaurant.backend.domains.dto.Recipe.dto.CreateMenuItemWithOneRecipeDto;
import com.restaurant.backend.domains.dto.Recipe.dto.CreateRecipeDto;
import com.restaurant.backend.domains.entities.Ingredient;
import com.restaurant.backend.domains.entities.MenuItem;
import com.restaurant.backend.domains.entities.Recipe;
import com.restaurant.backend.domains.entities.RecipeId;
import com.restaurant.backend.mappers.impl.MenuItemMapper;
import com.restaurant.backend.mappers.impl.RecipeMapper;
import com.restaurant.backend.services.IngredientService;
import com.restaurant.backend.services.MenuItemService;
import com.restaurant.backend.services.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
public class RecipeController {
    private final RecipeService recipeService;
    private final RecipeMapper recipeMapper;
    private final MenuItemService menuItemService;
    private final IngredientService ingredientService;
    private final MenuItemMapper menuItemMapper;
    public RecipeController(RecipeService recipeService, RecipeMapper recipeMapper, MenuItemService menuItemService,
                            MenuItemMapper menuItemMapper ,IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.recipeMapper = recipeMapper;
        this.menuItemService = menuItemService;
        this.ingredientService = ingredientService;
        this.menuItemMapper = menuItemMapper;
    }

    private boolean validateMenuItem(Optional<MenuItem> menuItem) {
        return menuItem.isPresent() && !Boolean.TRUE.equals(menuItem.get().getIsdeleted()) && "FOOD".equals(menuItem.get().getItemType().name());
    }
    private boolean validateIngredient(Optional<Ingredient> ingredient) {
        return ingredient.isPresent() && !Boolean.TRUE.equals(ingredient.get().getIsdeleted());
    }

    // add one recipe to brand new item
    @PostMapping(path="/recipes/one")
    public ResponseEntity<RecipeDto> addOneRecipe(@RequestBody CreateMenuItemWithOneRecipeDto body) {
        CreateMenuItemDto createMenuItemDto = body.getMenuItem();
        CreateRecipeDto createRecipeDto = body.getRecipe();

        if (!"FOOD".equals(createMenuItemDto.getItemType().name())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // create new menu item
        MenuItem newMenuItem = this.menuItemMapper.mapTo(createMenuItemDto);
        MenuItem savedMenuItem = this.menuItemService.save(newMenuItem);

        // add one recipe
        Optional<Ingredient> existedIngre = this.ingredientService.findById(createRecipeDto.getIngreId());
        if(!validateIngredient(existedIngre)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Recipe recipe = Recipe.builder()
                .id(new RecipeId(savedMenuItem.getId(), existedIngre.get().getId()))
                .item(savedMenuItem)
                .ingre(existedIngre.get())
                .ingreQuantityKg(createRecipeDto.getIngreQuantityKg())
                .build();

        // save recipe
        Recipe savedRecipe = this.recipeService.save(recipe);

        return new ResponseEntity<>(this.recipeMapper.mapFrom(savedRecipe), HttpStatus.CREATED);
    }

    // add many recipes to brand new menu item
    @PostMapping(path="/recipes/many")
    public ResponseEntity<List<RecipeDto>> addManyRecipes(@RequestBody CreateMenuItemWithManyRecipesDto body) {
        CreateMenuItemDto createMenuItemDto = body.getMenuItem();
        CreateManyRecipeDto createManyRecipeDto = body.getRecipes();

        if (!"FOOD".equals(createMenuItemDto.getItemType().name())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // create menu item
        MenuItem newMenuItem = this.menuItemMapper.mapTo(createMenuItemDto);
        MenuItem savedMenuItem = this.menuItemService.save(newMenuItem);

        // add many recipes
        List<Recipe> recipes = createManyRecipeDto.getIngredients().stream().map(ingreDto -> {
            Optional<Ingredient> existedIngre = this.ingredientService.findById(ingreDto.getIngreId());

            if(!validateIngredient(existedIngre)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            return Recipe.builder()
                    .id(new RecipeId(savedMenuItem.getId(), ingreDto.getIngreId()))
                    .ingre(existedIngre.get())
                    .item(savedMenuItem)
                    .ingreQuantityKg(ingreDto.getIngreQuantityKg())
                    .build();


        }).toList();

        // save many recipes
        List<Recipe> savedRecipes = this.recipeService.saveAll(recipes);

        return new ResponseEntity<>(savedRecipes.stream().map(recipeMapper::mapFrom).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping(path="/recipes/one/{menuItemId}")
    public ResponseEntity<RecipeDto> addOneRecipeToMenuItem(@RequestBody CreateRecipeDto createRecipeDto, @PathVariable int menuItemId){
        Optional<MenuItem> existedMenuItem = this.menuItemService.findById(menuItemId);
        if(!validateMenuItem(existedMenuItem)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Ingredient> existedIngre = this.ingredientService.findById(createRecipeDto.getIngreId());
        if(!validateIngredient(existedIngre)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Recipe> foundRecipe = this.recipeService.findById(new RecipeId(existedMenuItem.get().getId(), existedIngre.get().getId()));
        Recipe addedRecipe = null;
        // if exist before add to it
        if(foundRecipe.isPresent()){
            addedRecipe = Recipe.builder()
                    .item(existedMenuItem.get())
                    .ingre(existedIngre.get())
                    .id(new RecipeId(existedMenuItem.get().getId(), existedIngre.get().getId()))
                    .ingreQuantityKg(createRecipeDto.getIngreQuantityKg() + foundRecipe.get().getIngreQuantityKg())
                    .build();
        }
        // if not create a new one
        else addedRecipe = Recipe.builder()
                .item(existedMenuItem.get())
                .ingre(existedIngre.get())
                .id(new RecipeId(existedMenuItem.get().getId(), existedIngre.get().getId()))
                .ingreQuantityKg(createRecipeDto.getIngreQuantityKg())
                .build();
        Recipe savedRecipe = this.recipeService.save(addedRecipe);
        return new ResponseEntity<>(this.recipeMapper.mapFrom(savedRecipe), HttpStatus.CREATED);
    }

    @PostMapping(path="/recipes/many/{menuItemId}")
    public ResponseEntity<List<RecipeDto>> addManyRecipesToMenuItem(@RequestBody CreateManyRecipeDto createManyRecipeDto, @PathVariable int menuItemId){
        Optional<MenuItem> existedMenuItem = this.menuItemService.findById(menuItemId);
        if(!validateMenuItem(existedMenuItem)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Recipe> addedRecipes = createManyRecipeDto.getIngredients().stream()
                .map(ingreDto -> {
                    Optional<Ingredient> existedIngre = this.ingredientService.findById(ingreDto.getIngreId());
                    if(!validateIngredient(existedIngre)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                    }
                    Optional<Recipe> foundRecipe = this.recipeService.findById(new RecipeId(existedMenuItem.get().getId(), existedIngre.get().getId()));
                    if(foundRecipe.isPresent()){
                        return Recipe.builder()
                                .item(existedMenuItem.get())
                                .ingre(existedIngre.get())
                                .id(new RecipeId(existedMenuItem.get().getId(), existedIngre.get().getId()))
                                .ingreQuantityKg(ingreDto.getIngreQuantityKg() + foundRecipe.get().getIngreQuantityKg())
                                .build();
                    }
                    return Recipe.builder()
                            .item(existedMenuItem.get())
                            .ingre(existedIngre.get())
                            .id(new RecipeId(existedMenuItem.get().getId(), existedIngre.get().getId()))
                            .ingreQuantityKg(ingreDto.getIngreQuantityKg())
                            .build();
                }).toList();
        List<Recipe> savedRecipes = this.recipeService.saveAll(addedRecipes);
        return new ResponseEntity<>(savedRecipes.stream().map(recipeMapper::mapFrom).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(path="/recipes/{menuItemId}")
    public ResponseEntity<List<RecipeDto>> getAllRecipes(@PathVariable int menuItemId) {
        // find menu item first
        Optional<MenuItem> existedMenuItem = this.menuItemService.findById(menuItemId);
        if(!validateMenuItem(existedMenuItem)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Recipe> recipes = this.recipeService.findAllByItemId(menuItemId);
        return new ResponseEntity<>(recipes.stream().map(recipeMapper::mapFrom).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PutMapping(path="/recipes/{menuItemId}")
    public ResponseEntity<List<RecipeDto>> updateAllRecipes(@PathVariable int menuItemId,@RequestBody CreateManyRecipeDto createManyRecipeDto) {
        Optional<MenuItem> existedMenuItem = this.menuItemService.findById(menuItemId);
        if(!validateMenuItem(existedMenuItem)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // delete the old ones first
        List<Recipe> oldRecipes = this.recipeService.findAllByItemId(menuItemId);
        this.recipeService.deleteAll(oldRecipes);

        List<Recipe> recipes = createManyRecipeDto.getIngredients().stream().map(ingreDto -> {
            Optional<Ingredient> existedIngre = this.ingredientService.findById(ingreDto.getIngreId());
            if(!validateIngredient(existedIngre)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            return Recipe.builder().id(new RecipeId(menuItemId, existedIngre.get().getId()))
                    .ingre(existedIngre.get())
                    .ingreQuantityKg(ingreDto.getIngreQuantityKg())
                    .item(existedMenuItem.get())
                    .build();

        }).toList();
        // save all
        List<Recipe> savedRecipes = this.recipeService.saveAll(recipes);
        return new ResponseEntity<>(savedRecipes.stream().map(recipeMapper::mapFrom).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PatchMapping(path="/recipes/{menuItemId}/{ingreId}")
    public ResponseEntity<RecipeDto> updateOneRecipe(@PathVariable int menuItemId, @PathVariable int ingreId, @RequestBody CreateRecipeDto createRecipeDto) {
        Optional<MenuItem> existedMenuItem = this.menuItemService.findById(menuItemId);
        if(!validateMenuItem(existedMenuItem)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Ingredient> existedIngre = this.ingredientService.findById(ingreId);
        if(!validateIngredient(existedIngre)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Recipe> dbRecipe = this.recipeService.findById(new RecipeId(menuItemId, ingreId));
        if(!dbRecipe.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(createRecipeDto.getIngreQuantityKg()!=null){
            dbRecipe.get().setIngreQuantityKg(createRecipeDto.getIngreQuantityKg());
        }
        Recipe savedRecipe = this.recipeService.save(dbRecipe.get());
        return new ResponseEntity<>(this.recipeMapper.mapFrom(savedRecipe), HttpStatus.OK);
//        Recipe updatedRecipe = this.recipeMapper.mapTo(createRecipeDto);
//        updatedRecipe.setId(new RecipeId(menuItemId, ingreId));
//        Recipe savedRecipe = this.recipeService.save(updatedRecipe);
//        return new ResponseEntity<>(this.recipeMapper.mapFrom(savedRecipe), HttpStatus.OK);
    }

    @DeleteMapping(path="/recipes/{menuItemId}/{ingreId}")
    public ResponseEntity<Boolean> deleteOneRecipe(@PathVariable int menuItemId, @PathVariable int ingreId) {
        Optional<MenuItem> existedMenuItem = this.menuItemService.findById(menuItemId);
        if(!validateMenuItem(existedMenuItem)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Recipe> dbRecipe = this.recipeService.findById(new RecipeId(menuItemId, ingreId));
        if(!dbRecipe.isPresent()) {
            return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
        }
        this.recipeService.deleteById(dbRecipe.get().getId());
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
    @DeleteMapping(path="/recipes/{menuItemId}")
    public ResponseEntity<Boolean> deleteAllRecipes(@PathVariable int menuItemId) {
        Optional<MenuItem> existedMenuItem = this.menuItemService.findById(menuItemId);
        if(!validateMenuItem(existedMenuItem)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Recipe> oldRecipes = this.recipeService.findAllByItemId(menuItemId);
        this.recipeService.deleteAll(oldRecipes);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
