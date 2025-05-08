package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.Ingredient.IngredientDto;
import com.restaurant.backend.domains.dto.Ingredient.dto.CreateIngreDto;
import com.restaurant.backend.services.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class IngredientController {
    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping(path="/ingredients")
    public ResponseEntity<IngredientDto> addIngredient(@RequestBody CreateIngreDto createIngreDto) {
        IngredientDto saved = this.ingredientService.createIngredient(createIngreDto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<IngredientDto> getIngredient(@PathVariable int id) {
        IngredientDto ingredient = this.ingredientService.getIngredientById(id);
        return ingredient != null ? new ResponseEntity<>(ingredient, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(path="/ingredients/{id}")
    public ResponseEntity<IngredientDto> updateIngredient(@PathVariable int id, @RequestBody CreateIngreDto createIngreDto) {
        IngredientDto updated = this.ingredientService.updateIngredient(id, createIngreDto);
        return updated != null ? new ResponseEntity<>(updated, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(path="/ingredients/{id}")
    public ResponseEntity<IngredientDto> partialUpdateIngredient(@PathVariable int id, @RequestBody CreateIngreDto createIngreDto) {
        IngredientDto updated = this.ingredientService.partialUpdateIngredient(id, createIngreDto);
        return updated != null ? new ResponseEntity<>(updated, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(path = "/ingredients/{id}")
    public ResponseEntity<Boolean> deleteIngredient(@PathVariable int id) {
        boolean deleted = this.ingredientService.softDeleteIngredient(id);
        return new ResponseEntity<>(deleted, deleted ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping(path="/ingredients")
    public ResponseEntity<List<IngredientDto>> getAllIngredients() {
        return new ResponseEntity<>(this.ingredientService.getAllIngredients(), HttpStatus.OK);
    }
}