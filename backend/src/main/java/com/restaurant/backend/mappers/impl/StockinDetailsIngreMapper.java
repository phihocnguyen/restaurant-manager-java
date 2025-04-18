package com.restaurant.backend.mappers.impl;
import com.restaurant.backend.mappers.Mapper;
import com.restaurant.backend.domains.dto.StockinDetailsIngreDto;
import com.restaurant.backend.domains.entities.StockinDetailsIngre;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class StockinDetailsIngreMapper implements Mapper<StockinDetailsIngre, StockinDetailsIngreDto> {
    private ModelMapper modelMapper = new ModelMapper();
    public StockinDetailsIngreMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public StockinDetailsIngreDto mapFrom(StockinDetailsIngre stockinDetailsIngre) {
        return modelMapper.map(stockinDetailsIngre, StockinDetailsIngreDto.class);
    }

    @Override
    public StockinDetailsIngre mapTo(StockinDetailsIngreDto stockinDetailsIngreDto) {
        return modelMapper.map(stockinDetailsIngreDto, StockinDetailsIngre.class);
    }
}
