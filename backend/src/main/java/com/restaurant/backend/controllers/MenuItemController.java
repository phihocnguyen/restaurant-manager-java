package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.MenuItem.MenuItemDto;
import com.restaurant.backend.domains.dto.MenuItem.dto.CreateMenuItemDto;
import com.restaurant.backend.domains.dto.MenuItem.dto.UpdateMenuItemDto;
import com.restaurant.backend.services.MenuItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MenuItemController {
    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @PostMapping(path="/items")
    public ResponseEntity<MenuItemDto> addMenuItem(@RequestBody CreateMenuItemDto createMenuItemDto) {
        MenuItemDto saved = this.menuItemService.createMenuItem(createMenuItemDto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping(path="/items/{id}")
    public ResponseEntity<MenuItemDto> getMenuItem(@PathVariable int id) {
        MenuItemDto item = this.menuItemService.getMenuItemById(id);
        return item != null ? new ResponseEntity<>(item, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path="/items")
    public ResponseEntity<List<MenuItemDto>> getAllMenuItems() {
        return new ResponseEntity<>(this.menuItemService.getAllMenuItems(), HttpStatus.OK);
    }

    @PutMapping(path="/items/{id}")
    public ResponseEntity<MenuItemDto> updateMenuItem(@RequestBody UpdateMenuItemDto updateMenuItemDto, @PathVariable int id) {
        MenuItemDto updated = this.menuItemService.updateMenuItem(id, updateMenuItemDto);
        return updated != null ? new ResponseEntity<>(updated, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(path="/items/{id}")
    public ResponseEntity<MenuItemDto> partialUpdateMenuItem(@PathVariable int id, @RequestBody UpdateMenuItemDto updateMenuItemDto) {
        MenuItemDto updated = this.menuItemService.partialUpdateMenuItem(id, updateMenuItemDto);
        return updated != null ? new ResponseEntity<>(updated, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(path="/items/{id}")
    public ResponseEntity<Boolean> deleteMenuItem(@PathVariable int id) {
        boolean deleted = this.menuItemService.softDeleteMenuItem(id);
        return new ResponseEntity<>(deleted, deleted ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}