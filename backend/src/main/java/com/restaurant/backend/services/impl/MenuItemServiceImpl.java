package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.entities.MenuItem;
import com.restaurant.backend.repositories.MenuItemRepository;
import com.restaurant.backend.services.MenuItemService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository menuItemRepository;
    public MenuItemServiceImpl(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public MenuItem save(MenuItem menuItem){
        return this.menuItemRepository.save(menuItem);
    }

    public Optional<MenuItem> findOneById(int id) {
        return this.menuItemRepository.findById(id);
    }
}
