package com.restaurant.backend.services;

import com.restaurant.backend.domains.entities.MenuItem;
import java.util.Optional;

public interface MenuItemService {
    public MenuItem save(MenuItem menuItem);
    public Optional<MenuItem> findOneById(int id);
}
