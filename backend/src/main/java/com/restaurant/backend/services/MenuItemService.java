package com.restaurant.backend.services;

import com.restaurant.backend.domains.dto.MenuItem.MenuItemDto;
import com.restaurant.backend.domains.dto.MenuItem.dto.CreateMenuItemDto;
import com.restaurant.backend.domains.dto.MenuItem.dto.CreateMenuItemFormDataDto;
import com.restaurant.backend.domains.dto.MenuItem.dto.UpdateMenuItemDto;
import com.restaurant.backend.domains.entities.MenuItem;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface MenuItemService {
    public MenuItemDto createMenuItem(CreateMenuItemDto dto);
    public MenuItemDto createMenuItem(CreateMenuItemFormDataDto dto) throws IOException;
    public MenuItemDto getMenuItemById(int id);
    public List<MenuItemDto> getAllMenuItems();
    public MenuItemDto updateMenuItem(int id, UpdateMenuItemDto dto);
    public MenuItemDto partialUpdateMenuItem(int id, UpdateMenuItemDto dto);
    public boolean softDeleteMenuItem(int id);
    MenuItem saveEntity(MenuItem entity);
    Optional<MenuItem> findById(int id);
}
