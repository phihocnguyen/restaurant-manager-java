package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.MenuItem.MenuItemDto;
import com.restaurant.backend.domains.dto.MenuItem.dto.CreateMenuItemDto;
import com.restaurant.backend.domains.entities.MenuItem;
import com.restaurant.backend.mappers.impl.MenuItemMapper;
import com.restaurant.backend.services.MenuItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        MenuItem menuItem = this.menuItemMapper.mapTo(createMenuItemDto);
        MenuItem savedMenuItem = this.menuItemService.save(menuItem);
        return new ResponseEntity<>(this.menuItemMapper.mapFrom(savedMenuItem), HttpStatus.CREATED);
    }

    @GetMapping(path="/items/{id}")
    public ResponseEntity<MenuItemDto> getMenuItem(@PathVariable int id){
        Optional<MenuItem> dbMenuItem = this.menuItemService.findById(id);
        if(!dbMenuItem.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(this.menuItemMapper.mapFrom(dbMenuItem.get()), HttpStatus.OK);
    }

    @GetMapping(path="/items")
    public ResponseEntity<List<MenuItemDto>> getAllMenuItems(){
        return new ResponseEntity<>(this.menuItemService.findAll().stream().map(this.menuItemMapper::mapFrom).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PutMapping(path="/items/{id}")
    public ResponseEntity<MenuItemDto> updateMenuItem(@RequestBody CreateMenuItemDto createMenuItemDto, @PathVariable int id){
        Optional<MenuItem> dbMenuItem = this.menuItemService.findById(id);
        if(!dbMenuItem.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        MenuItem updatedMenuItem = this.menuItemMapper.mapTo(createMenuItemDto);
        updatedMenuItem.setId(id);
        MenuItem savedMenuItem = this.menuItemService.save(updatedMenuItem);
        return new ResponseEntity<>(this.menuItemMapper.mapFrom(savedMenuItem), HttpStatus.OK);
    }

    @PatchMapping(path="/items/{id}")
    public ResponseEntity<MenuItemDto> partialUpdateMenuItem(@PathVariable int id, @RequestBody CreateMenuItemDto createMenuItemDto){
        Optional<MenuItem> dbMenuItem = this.menuItemService.findById(id);
        if(!dbMenuItem.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(createMenuItemDto.getItemName() != null){
            dbMenuItem.get().setItemName(createMenuItemDto.getItemName());
        }
        if(createMenuItemDto.getItemCprice() != null){
            dbMenuItem.get().setItemCprice(createMenuItemDto.getItemCprice());
        }
        if(createMenuItemDto.getItemImg() != null){
            dbMenuItem.get().setItemImg(createMenuItemDto.getItemImg());
        }
        if(createMenuItemDto.getItemType() != null){
            dbMenuItem.get().setItemType(
                    com.restaurant.backend.domains.entities.ItemType.valueOf(createMenuItemDto.getItemType().name())
            );
        }
        if(createMenuItemDto.getInstock() != null){
            dbMenuItem.get().setInstock(createMenuItemDto.getInstock());
        }
        if(createMenuItemDto.getIsdeleted() != null){
            dbMenuItem.get().setIsdeleted(createMenuItemDto.getIsdeleted());
        }
        MenuItem savedMenuItem = this.menuItemService.save(dbMenuItem.get());
        return new ResponseEntity<>(this.menuItemMapper.mapFrom(savedMenuItem), HttpStatus.OK);
    }

    @DeleteMapping(path="/items/{id}")
    public ResponseEntity<Boolean> deleteMenuItem (@PathVariable int id){
        Optional<MenuItem> dbMenuItem = this.menuItemService.findById(id);
        if(!dbMenuItem.isPresent()){
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
        dbMenuItem.get().setIsdeleted(true);
        MenuItem savedMenuItem = this.menuItemService.save(dbMenuItem.get());
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
