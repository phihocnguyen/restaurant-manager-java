package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.dto.Recipe.RecipeDto;
import com.restaurant.backend.domains.dto.Recipe.dto.*;
import com.restaurant.backend.domains.entities.*;
import com.restaurant.backend.mappers.impl.IngredientMapper;
import com.restaurant.backend.mappers.impl.MenuItemMapper;
import com.restaurant.backend.mappers.impl.RecipeMapper;
import com.restaurant.backend.repositories.RecipeRepository;
import com.restaurant.backend.services.IngredientService;
import com.restaurant.backend.services.MenuItemService;
import com.restaurant.backend.services.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final IngredientService ingredientService;
    private final MenuItemService menuItemService;
    private final IngredientMapper ingredientMapper;
    private final MenuItemMapper menuItemMapper;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeMapper recipeMapper, IngredientService ingredientService,
                             MenuItemService menuItemService, IngredientMapper ingredientMapper, MenuItemMapper menuItemMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.ingredientService = ingredientService;
        this.menuItemService = menuItemService;
        this.ingredientMapper = ingredientMapper;
        this.menuItemMapper = menuItemMapper;
    }

    private boolean validIngredient(Optional<Ingredient> ingre) {
        return ingre.isPresent() && !Boolean.TRUE.equals(ingre.get().getIsdeleted());
    }

    private boolean validFood(Optional<MenuItem> item) {
        return item.isPresent() && !Boolean.TRUE.equals(item.get().getIsdeleted()) && item.get().getItemType() == ItemType.FOOD;
    }

    private BigDecimal calculateCprice(List<Recipe> recipes) {
        return recipes.stream()
                .map(r -> r.getIngre().getIngrePrice().multiply(BigDecimal.valueOf(r.getIngreQuantityKg())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public RecipeDto addOneRecipeWithMenu(CreateMenuItemWithOneRecipeDto body) {
        MenuItem item = menuItemMapper.mapTo(body.getMenuItem());
        if (item.getItemType() != ItemType.FOOD) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        Ingredient ingre = ingredientMapper.mapTo(ingredientService.getIngredientById(body.getRecipe().getIngreId()));
        if (!validIngredient(Optional.ofNullable(ingre))) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        item.setItemCprice(ingre.getIngrePrice().multiply(BigDecimal.valueOf(body.getRecipe().getIngreQuantityKg())));
        MenuItem savedItem = menuItemService.saveEntity(item);

        Recipe recipe = Recipe.builder()
                .id(new RecipeId(savedItem.getId(), ingre.getId()))
                .item(savedItem)
                .ingre(ingre)
                .ingreQuantityKg(body.getRecipe().getIngreQuantityKg())
                .build();

        return recipeMapper.mapFrom(recipeRepository.save(recipe));
    }

    @Override
    public Recipe save(Recipe recipe) {
        return this.recipeRepository.save(recipe);
    }

    @Override
    public List<RecipeDto> addManyRecipesWithMenu(CreateMenuItemWithManyRecipesDto body) {
        MenuItem item = menuItemMapper.mapTo(body.getMenuItem());
        if (item.getItemType() != ItemType.FOOD) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        MenuItem savedItem = menuItemService.saveEntity(item);
        List<Recipe> recipes = body.getRecipes().getIngredients().stream().map(dto -> {
            Ingredient ingre = ingredientMapper.mapTo(ingredientService.getIngredientById(dto.getIngreId()));
            if (!validIngredient(Optional.ofNullable(ingre))) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            return Recipe.builder()
                    .id(new RecipeId(savedItem.getId(), ingre.getId()))
                    .item(savedItem)
                    .ingre(ingre)
                    .ingreQuantityKg(dto.getIngreQuantityKg())
                    .build();
        }).collect(Collectors.toList());

        savedItem.setItemCprice(calculateCprice(recipes));
        menuItemService.saveEntity(savedItem);
        return recipeRepository.saveAll(recipes).stream().map(recipeMapper::mapFrom).collect(Collectors.toList());
    }

    @Override
    public RecipeDto addOneToExisting(int menuItemId, CreateRecipeDto dto) {
        Optional<MenuItem> item = menuItemService.findById(menuItemId);
        if (!validFood(item)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        Ingredient ingre = ingredientMapper.mapTo(ingredientService.getIngredientById(dto.getIngreId()));
        if (!validIngredient(Optional.ofNullable(ingre))) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        Optional<Recipe> existing = recipeRepository.findById(new RecipeId(menuItemId, ingre.getId()));
        Recipe recipe = existing.map(r -> {
            r.setIngreQuantityKg(r.getIngreQuantityKg() + dto.getIngreQuantityKg());
            return r;
        }).orElseGet(() -> Recipe.builder()
                .id(new RecipeId(menuItemId, ingre.getId()))
                .item(item.get())
                .ingre(ingre)
                .ingreQuantityKg(dto.getIngreQuantityKg())
                .build());

        Recipe saved = recipeRepository.save(recipe);
        item.get().setItemCprice(calculateCprice(recipeRepository.findAllByItemId(menuItemId)));
        menuItemService.saveEntity(item.get());
        return recipeMapper.mapFrom(saved);
    }

    @Override
    public List<RecipeDto> addManyToExisting(int menuItemId, CreateManyRecipesDto dto) {
        Optional<MenuItem> item = menuItemService.findById(menuItemId);
        if (!validFood(item)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        List<Recipe> recipes = dto.getIngredients().stream().map(ingreDto -> {
            Ingredient ingre = ingredientMapper.mapTo(ingredientService.getIngredientById(ingreDto.getIngreId()));
            if (!validIngredient(Optional.ofNullable(ingre))) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

            Optional<Recipe> existing = recipeRepository.findById(new RecipeId(menuItemId, ingre.getId()));
            return existing.map(r -> {
                r.setIngreQuantityKg(r.getIngreQuantityKg() + ingreDto.getIngreQuantityKg());
                return r;
            }).orElseGet(() -> Recipe.builder()
                    .id(new RecipeId(menuItemId, ingre.getId()))
                    .item(item.get())
                    .ingre(ingre)
                    .ingreQuantityKg(ingreDto.getIngreQuantityKg())
                    .build());
        }).collect(Collectors.toList());

        item.get().setItemCprice(calculateCprice(recipeRepository.findAllByItemId(menuItemId)));
        menuItemService.saveEntity(item.get());
        return recipeRepository.saveAll(recipes).stream().map(recipeMapper::mapFrom).collect(Collectors.toList());
    }

    @Override
    public List<RecipeDto> getAllByMenuItemId(int menuItemId) {
        return recipeRepository.findAllByItemId(menuItemId).stream().map(recipeMapper::mapFrom).collect(Collectors.toList());
    }

    @Override
    public List<RecipeDto> updateAll(int menuItemId, CreateManyRecipesDto dto) {
        Optional<MenuItem> item = menuItemService.findById(menuItemId);
        if (!validFood(item)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        recipeRepository.deleteAll(recipeRepository.findAllByItemId(menuItemId));
        List<Recipe> newList = dto.getIngredients().stream().map(ingreDto -> {
            Ingredient ingre = ingredientMapper.mapTo(ingredientService.getIngredientById(ingreDto.getIngreId()));
            return Recipe.builder()
                    .id(new RecipeId(menuItemId, ingre.getId()))
                    .item(item.get())
                    .ingre(ingre)
                    .ingreQuantityKg(ingreDto.getIngreQuantityKg())
                    .build();
        }).collect(Collectors.toList());

        item.get().setItemCprice(calculateCprice(newList));
        menuItemService.saveEntity(item.get());
        return recipeRepository.saveAll(newList).stream().map(recipeMapper::mapFrom).collect(Collectors.toList());
    }

    @Override
    public RecipeDto updateOne(int menuItemId, int ingreId, CreateRecipeDto dto) {
        Optional<Recipe> db = recipeRepository.findById(new RecipeId(menuItemId, ingreId));
        if (!db.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        db.get().setIngreQuantityKg(dto.getIngreQuantityKg());
        Optional<MenuItem> item = menuItemService.findById(menuItemId);
        item.ifPresent(i -> {
            i.setItemCprice(calculateCprice(recipeRepository.findAllByItemId(menuItemId)));
            menuItemService.saveEntity(i);
        });
        return recipeMapper.mapFrom(recipeRepository.save(db.get()));
    }

    @Override
    public boolean deleteOne(int menuItemId, int ingreId) {
        Optional<Recipe> found = recipeRepository.findById(new RecipeId(menuItemId, ingreId));
        if (!found.isPresent()) return false;
        recipeRepository.deleteById(found.get().getId());
        menuItemService.findById(menuItemId).ifPresent(i -> {
            i.setItemCprice(calculateCprice(recipeRepository.findAllByItemId(menuItemId)));
            menuItemService.saveEntity(i);
        });
        return true;
    }

    @Override
    public boolean deleteAll(int menuItemId) {
        Optional<MenuItem> item = menuItemService.findById(menuItemId);
        if (!validFood(item)) return false;
        recipeRepository.deleteAll(recipeRepository.findAllByItemId(menuItemId));
        item.get().setItemCprice(BigDecimal.ZERO);
        menuItemService.saveEntity(item.get());
        return true;
    }

    @Override
    public Optional<Recipe> findById(RecipeId recipeId) {
        return this.recipeRepository.findById(recipeId);
    }
}