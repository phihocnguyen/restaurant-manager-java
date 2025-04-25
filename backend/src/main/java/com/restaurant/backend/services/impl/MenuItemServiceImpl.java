package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.dto.MenuItem.MenuItemDto;
import com.restaurant.backend.domains.dto.MenuItem.dto.CreateMenuItemDto;
import com.restaurant.backend.domains.dto.MenuItem.dto.UpdateMenuItemDto;
import com.restaurant.backend.domains.entities.ItemType;
import com.restaurant.backend.domains.entities.MenuItem;
import com.restaurant.backend.mappers.impl.MenuItemMapper;
import com.restaurant.backend.repositories.MenuItemRepository;
import com.restaurant.backend.services.MenuItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final MenuItemMapper menuItemMapper;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository, MenuItemMapper menuItemMapper) {
        this.menuItemRepository = menuItemRepository;
        this.menuItemMapper = menuItemMapper;
    }

    public MenuItemDto createMenuItem(CreateMenuItemDto dto) {
        return menuItemMapper.mapFrom(menuItemRepository.save(menuItemMapper.mapTo(dto)));
    }

    public MenuItemDto getMenuItemById(int id) {
        Optional<MenuItem> found = menuItemRepository.findById(id);
        return found.isPresent() ? menuItemMapper.mapFrom(found.get()) : null;
    }

    public List<MenuItemDto> getAllMenuItems() {
        return menuItemRepository.findAll().stream()
                .filter(item -> !Boolean.TRUE.equals(item.getIsdeleted()))
                .map(menuItemMapper::mapFrom)
                .collect(Collectors.toList());
    }


    public MenuItemDto updateMenuItem(int id, CreateMenuItemDto dto) {
        Optional<MenuItem> found = menuItemRepository.findById(id);
        if (!found.isPresent()) return null;

        MenuItem updated = menuItemMapper.mapTo(dto);
        updated.setId(id);
        return menuItemMapper.mapFrom(menuItemRepository.save(updated));
    }

    public MenuItemDto partialUpdateMenuItem(int id, UpdateMenuItemDto dto) {
        Optional<MenuItem> found = menuItemRepository.findById(id);
        if (!found.isPresent()) return null;

        MenuItem item = found.get();
        if (dto.getItemName() != null) item.setItemName(dto.getItemName());
        if (dto.getItemCprice() != null) item.setItemCprice(dto.getItemCprice());
        if (dto.getItemImg() != null) item.setItemImg(dto.getItemImg());
        if (dto.getInstock() != null) item.setInstock(dto.getInstock());
        if (dto.getIsdeleted() != null) item.setIsdeleted(dto.getIsdeleted());

        return menuItemMapper.mapFrom(menuItemRepository.save(item));
    }

    public boolean softDeleteMenuItem(int id) {
        Optional<MenuItem> found = menuItemRepository.findById(id);
        if (!found.isPresent()) return false;

        MenuItem item = found.get();
        item.setIsdeleted(true);
        menuItemRepository.save(item);
        return true;
    }

    public MenuItem saveEntity(MenuItem entity) {
        return menuItemRepository.save(entity);
    }

    public Optional<MenuItem> findById(int id) {
        return menuItemRepository.findById(id);
    }

}

