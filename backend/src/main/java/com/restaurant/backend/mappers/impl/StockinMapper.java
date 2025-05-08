package com.restaurant.backend.mappers.impl;

import com.restaurant.backend.domains.dto.Stockin.StockinDto;
import com.restaurant.backend.domains.entities.Stockin;
import com.restaurant.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class StockinMapper implements Mapper<Stockin, StockinDto> {
    private ModelMapper modelMapper = new ModelMapper();
    public StockinMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    @Override
    public StockinDto mapFrom(Stockin stockin) {
        return modelMapper.map(stockin, StockinDto.class);
    }

    @Override
    public Stockin mapTo(StockinDto stockinDto) {
        return modelMapper.map(stockinDto, Stockin.class);
    }
}
