package com.restaurant.backend.services.impl;

import com.restaurant.backend.repositories.MenuItemRepository;
import com.restaurant.backend.services.MenuItemService;
import org.springframework.stereotype.Service;

@Service
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository menuItemRepository;
    public MenuItemServiceImpl(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }
}
