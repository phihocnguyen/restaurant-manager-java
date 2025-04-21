package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.MenuItem.MenuItemDto;
import com.restaurant.backend.domains.dto.MenuItem.dto.CreateMenuItemDto;
import com.restaurant.backend.domains.entities.MenuItem;
import com.restaurant.backend.mappers.impl.MenuItemMapper;
import com.restaurant.backend.services.MenuItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class MenuItemController {
    private final MenuItemService menuItemService;
    private final MenuItemMapper menuItemMapper;
    public MenuItemController(MenuItemService menuItemService, MenuItemMapper menuItemMapper) {
        this.menuItemService = menuItemService;
        this.menuItemMapper = menuItemMapper;
    }

    @PostMapping(path="/items")
    public ResponseEntity<MenuItemDto> addMenuItem(@RequestBody CreateMenuItemDto createMenuItemDto) {
        MenuItem menuItem = this.menuItemMapper.mapFrom(createMenuItemDto);
        MenuItem savedMenuItem = this.menuItemService.save(menuItem);
        return new ResponseEntity<>(this.menuItemMapper.mapTo(menuItem), HttpStatus.CREATED);
    }

    @GetMapping(path="/items/{id}")
    public ResponseEntity<Optional<MenuItemDto>> getMenuItem(@PathVariable int id){
        Optional<MenuItem> menuItem = this.menuItemService.findOneById(id);
        return null; // back later
    }
}
