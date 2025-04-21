package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.Ingredient.IngredientDto;
import com.restaurant.backend.domains.dto.Ingredient.dto.CreateIngreDto;
import com.restaurant.backend.domains.entities.Ingredient;
import com.restaurant.backend.mappers.impl.IngredientMapper;
import com.restaurant.backend.services.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class IngredientController {
    private final IngredientService ingredientService;
    private final IngredientMapper ingredientMapper;
    public IngredientController(IngredientService ingredientService, IngredientMapper ingredientMapper) {
        this.ingredientService = ingredientService;
        this.ingredientMapper = ingredientMapper;
    }
    @PostMapping(path="/ingredients")
    public ResponseEntity<IngredientDto> addIngredient(@RequestBody CreateIngreDto createIngreDto) {
        Ingredient ingredient = ingredientMapper.mapTo(createIngreDto);
        Ingredient savedIngredient = ingredientService.save(ingredient);
        return new ResponseEntity<>(this.ingredientMapper.mapFrom(savedIngredient), HttpStatus.CREATED);
    }
    @GetMapping("/ingredients/{id}")
    public ResponseEntity<IngredientDto> getIngredient(@PathVariable int id) {
        Optional<Ingredient> foundIngre = this.ingredientService.findById(id);
        if (!foundIngre.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(this.ingredientMapper.mapFrom(foundIngre.get()), HttpStatus.OK);
    }
    @PutMapping(path="/ingredients/{id}")
    public ResponseEntity<IngredientDto> updateIngredient(@PathVariable int id, @RequestBody CreateIngreDto createIngreDto) {
        Optional<Ingredient> dbIngredient = this.ingredientService.findById(id);
        if (!dbIngredient.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // update everything + set its id
        Ingredient updatedIngredient = ingredientMapper.mapTo(createIngreDto);
        updatedIngredient.setId(id);
        Ingredient savedIngredient = ingredientService.save(updatedIngredient);
        return new ResponseEntity<>(this.ingredientMapper.mapFrom(savedIngredient), HttpStatus.OK);
    }
    @PatchMapping(path="/ingredients/{id}")
    public ResponseEntity<IngredientDto> partialUpdateIngredient(@PathVariable int id, @RequestBody CreateIngreDto createIngreDto) {
        Optional<Ingredient> dbIngredient = this.ingredientService.findById(id);
        if (!dbIngredient.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // set it partially
        if(createIngreDto.getIngreName() != null) {
            dbIngredient.get().setIngreName(createIngreDto.getIngreName());
        }
        if(createIngreDto.getIngrePrice() != null) {
            dbIngredient.get().setIngrePrice(createIngreDto.getIngrePrice());
        }
        if(createIngreDto.getInstockKg() != null) {
            dbIngredient.get().setInstockKg(createIngreDto.getInstockKg());
        }
        if(createIngreDto.getIsdeleted() != null) {
            dbIngredient.get().setIsdeleted(createIngreDto.getIsdeleted());
        }
        Ingredient savedIngredient = ingredientService.save(dbIngredient.get());
        return new ResponseEntity<>(this.ingredientMapper.mapFrom(savedIngredient), HttpStatus.OK);
    }
    @DeleteMapping(path = "/ingredients/{id}")
    public ResponseEntity<Boolean> deleteIngredient(@PathVariable int id) {
        Optional<Ingredient> dbIngredient = this.ingredientService.findById(id);
        if (!dbIngredient.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        dbIngredient.get().setIsdeleted(true);
        Ingredient savedIngredient = ingredientService.save(dbIngredient.get());
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
    @GetMapping(path="/ingredients")
    public ResponseEntity<List<IngredientDto>> getAllIngredients() {
        return new ResponseEntity<>(this.ingredientService.findAll().stream().map(this.ingredientMapper::mapFrom).collect(Collectors.toList()), HttpStatus.OK);
    }
}
