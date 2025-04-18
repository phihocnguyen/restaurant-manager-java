package com.restaurant.backend.mappers.impl;

import com.restaurant.backend.domains.dto.StockinDetailsDrinkOtherDto;
import com.restaurant.backend.domains.entities.StockinDetailsDrinkOther;
import com.restaurant.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class StockinDetailsDrinkOtherMapper implements Mapper<StockinDetailsDrinkOther, StockinDetailsDrinkOtherDto> {
    private ModelMapper modelMapper = new ModelMapper();
    public StockinDetailsDrinkOtherMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public StockinDetailsDrinkOtherDto mapFrom(StockinDetailsDrinkOther stockinDetailsDrinkOther) {
        return modelMapper.map(stockinDetailsDrinkOther, StockinDetailsDrinkOtherDto.class);
    }

    @Override
    public StockinDetailsDrinkOther mapTo(StockinDetailsDrinkOtherDto stockinDetailsDrinkOtherDto) {
        return modelMapper.map(stockinDetailsDrinkOtherDto, StockinDetailsDrinkOther.class);
    }
}
