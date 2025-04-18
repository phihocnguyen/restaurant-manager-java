package com.restaurant.backend.mappers.impl;

import com.restaurant.backend.domains.dto.MenuItemDto;
import com.restaurant.backend.domains.entities.MenuItem;
import com.restaurant.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapper implements Mapper<MenuItemDto, MenuItem> {
    private ModelMapper modelMapper = new ModelMapper();
    public MenuItemMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    @Override
    public MenuItem mapFrom(MenuItemDto menuItemDto) {
        return modelMapper.map(menuItemDto, MenuItem.class);
    }

    @Override
    public MenuItemDto mapTo(MenuItem menuItem) {
        return modelMapper.map(menuItem, MenuItemDto.class);
    }
}
