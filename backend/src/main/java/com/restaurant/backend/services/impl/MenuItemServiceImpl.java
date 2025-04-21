package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.entities.MenuItem;
import com.restaurant.backend.repositories.MenuItemRepository;
import com.restaurant.backend.services.MenuItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository menuItemRepository;
    public MenuItemServiceImpl(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public MenuItem save(MenuItem menuItem){
        return this.menuItemRepository.save(menuItem);
    }

    public Optional<MenuItem> findById(int id) {
        return this.menuItemRepository.findById(id);
    }

    public List<MenuItem> findAll(){
        return this.menuItemRepository.findAll().stream().filter(item -> !item.getIsdeleted()).collect(Collectors.toList());
    }
}
