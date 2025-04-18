package com.restaurant.backend.controllers;

import com.restaurant.backend.services.MenuItemService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuItemController {
    private final MenuItemService menuItemService;
    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }
}
