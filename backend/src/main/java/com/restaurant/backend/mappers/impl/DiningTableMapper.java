package com.restaurant.backend.mappers.impl;

import com.restaurant.backend.domains.dto.DiningTable.DiningTableDto;
import com.restaurant.backend.domains.dto.DiningTable.dto.CreateDiningTableDto;
import com.restaurant.backend.domains.entities.DiningTable;
import com.restaurant.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DiningTableMapper implements Mapper<DiningTable, DiningTableDto> {
    private ModelMapper modelMapper = new ModelMapper();
    public DiningTableMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public DiningTableDto mapFrom(DiningTable diningTable) {
        return modelMapper.map(diningTable, DiningTableDto.class);
    }

    @Override
    public DiningTable mapTo(DiningTableDto diningTableDto) {
        return modelMapper.map(diningTableDto, DiningTable.class);
    }

    public DiningTable mapTo(CreateDiningTableDto createDiningTableDto) {
        return modelMapper.map(createDiningTableDto, DiningTable.class);
    }
}
