package com.restaurant.backend.mappers.impl;

import com.restaurant.backend.domains.dto.MenuItem.MenuItemDto;
import com.restaurant.backend.domains.dto.MenuItem.dto.CreateMenuItemDto;
import com.restaurant.backend.domains.dto.MenuItem.dto.UpdateMenuItemDto;
import com.restaurant.backend.domains.entities.MenuItem;
import com.restaurant.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapper implements Mapper<MenuItem, MenuItemDto> {
    private ModelMapper modelMapper = new ModelMapper();
    public MenuItemMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    @Override
    public MenuItem mapTo(MenuItemDto menuItemDto) {
        return modelMapper.map(menuItemDto, MenuItem.class);
    }

    @Override
    public MenuItemDto mapFrom(MenuItem menuItem) {
        return modelMapper.map(menuItem, MenuItemDto.class);
    }

    public MenuItem mapTo(CreateMenuItemDto createMenuItemDto) {return modelMapper.map(createMenuItemDto, MenuItem.class);}
    public MenuItem mapTo(UpdateMenuItemDto updateMenuItemDto) {return modelMapper.map(updateMenuItemDto, MenuItem.class);}
}
