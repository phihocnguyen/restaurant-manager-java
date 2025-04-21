package com.restaurant.backend.services;

import com.restaurant.backend.domains.entities.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemService {
    public MenuItem save(MenuItem menuItem);
    public Optional<MenuItem> findById(int id);
    public List<MenuItem> findAll();
}
